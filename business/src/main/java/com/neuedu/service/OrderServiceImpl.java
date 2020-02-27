package com.neuedu.service;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.OrderItemVo;
import com.neuedu.vo.OrderVo;
import com.neuedu.vo.ProductDetailVO;
import org.apache.ibatis.javassist.bytecode.StackMapTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ICartService iCartService;
    @Autowired
    IProductService iProductService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //1.参数非空校验
        if (shippingId == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_NOT_EMPTY.getStatus(),StatusEnum.ADDRESS_NOT_EMPTY.getDesc());
        }
        //2.根据userid查询购物车中已经选中的商品List<Cart>
        ServerResponse<List<Cart>> serverResponse = iCartService.findCartByUserIdAndChecked(userId);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<Cart> cartList = serverResponse.getData();
        if (cartList == null || cartList.size() == 0){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_CART_EMPTY.getStatus(),StatusEnum.USER_CART_EMPTY.getDesc());
        }

        //3.List<Cart>转换成List<OrderItem>
        ServerResponse<List<OrderItem>> serverResponse1 = assembleOrderItemList(cartList,userId);
        if (!serverResponse1.isSuccess()){
            return serverResponse1;
        }

        //4.生成订单，并插入订单库
        List<OrderItem> orderItemList = serverResponse1.getData();
        ServerResponse serverResponse2 = generateOrder(userId,orderItemList,shippingId);

        if (!serverResponse2.isSuccess()){
            return serverResponse2;
        }

        //5.将订单明细批量插入订单明细库
        Order order = (Order) serverResponse2.getData();
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }

        int count = orderItemMapper.insertBatch(orderItemList);

        if (count <= 0){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_ITEM_CREATE_FAIL.getStatus(),StatusEnum.ORDER_ITEM_CREATE_FAIL.getDesc());
        }

        //6.减商品库存
        reduceStock(orderItemList);

        //7清空购物车中已经下单的商品
        ServerResponse serverResponse3 = cleanCart(cartList);
        if (!serverResponse3.isSuccess()){
            return serverResponse3;
        }

        //8.前端返回OrderVo
        OrderVo orderVo = assembleOrderVo(order,orderItemList,shippingId);

        return ServerResponse.serverResponseBySuccess(null,orderVo);
    }

    @Override
    public ServerResponse cancelOrder(Long orderNo) {
        ///1.参数校验
        if (orderNo == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        //2.根据订单号查询订单是否存在
        Order order = orderMapper.findOrderByOrderNo(orderNo);
        if (order == null){//订单不存在
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_NOT_EXISTS.getStatus(),StatusEnum.ORDER_NOT_EXISTS.getDesc());
        }

        //只有未付款的订单才能取消
        if (order.getStatus() != Consts.OrderStatumEnum.UNPAY.getStatus()){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_NOT_CANCEL.getStatus(),StatusEnum.ORDER_NOT_CANCEL.getDesc());
        }

        //取消订单
        order.setStatus(Consts.OrderStatumEnum.CANCELED.getStatus());
        int count = orderMapper.updateByPrimaryKey(order);
        if (count <= 0){
            //订单取消失败
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_CANCEL_FAIL.getStatus(),StatusEnum.ORDER_CANCEL_FAIL.getDesc());
        }

        //更新库存
        List<OrderItem> orderItemList = orderItemMapper.findOrderItemByOrderNo(orderNo);
        for (OrderItem orderItem:orderItemList){
            Integer quantity = orderItem.getQuantity();
            Integer productId = orderItem.getProductId();

            ServerResponse response = iProductService.updateStock(productId,quantity,1);

            if (!response.isSuccess()){
                return ServerResponse.serverResponseByFail(StatusEnum.REDUCE_STOCK_FAIL.getStatus(),StatusEnum.REDUCE_STOCK_FAIL.getDesc());
            }
        }

        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse findOrderByOrderNo(Long orderNo) {
        //1.参数非空校验
        if(orderNo==null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        //2.根据订单号查询订单
        Order order = orderMapper.findOrderByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_NOT_EXISTS.getStatus(),StatusEnum.ORDER_NOT_EXISTS.getDesc());
        }
        List<OrderItem> orderItemList = orderItemMapper.findOrderItemByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList, order.getShippingId());

        return ServerResponse.serverResponseBySuccess(null,orderVo);
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void closeOrder(){
        System.out.println("=======closeOrder=======");
    }

    public OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setPaymentTime(DateUtils.date2Str(order.getPaymentTime()));
        orderVo.setSendTime(DateUtils.date2Str(order.getSendTime()));
        orderVo.setEndTime(DateUtils.date2Str(order.getEndTime()));
        orderVo.setCreateTime(DateUtils.date2Str(order.getCreateTime()));
        orderVo.setCloseTime(DateUtils.date2Str(order.getCloseTime()));

        orderVo.setShippingId(shippingId);

        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for (OrderItem orderItem:orderItemList){
            OrderItemVo orderItemVo = convertOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }

        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     * OrderItem-->orderItemVo
     */
    private OrderItemVo convertOrderItemVo(OrderItem orderItem){
        if (orderItem == null){
            return null;
        }
        OrderItemVo orderItemVo = new OrderItemVo();

        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setProductName(orderItem.getProductName());
//        orderItem.setProductId(orderItem.getProductId());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateUtils.date2Str(orderItem.getCreateTime()));
        return orderItemVo;
    }

    /***
     * 清空购物车已下单商品
     */
    private ServerResponse cleanCart(List<Cart> cartList){
        return iCartService.deleteBatchByIds(cartList);

    }

    /**
     * 扣库存
     */
    private ServerResponse reduceStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem:orderItemList){
            Integer productId = orderItem.getProductId();
            Integer quantity = orderItem.getQuantity();
            //根据商品id扣库存
            ServerResponse serverResponse = iProductService.updateStock(productId,quantity,0);
            if (!serverResponse.isSuccess()){
                return serverResponse;
            }
        }
        return ServerResponse.serverResponseBySuccess();
    }

    private ServerResponse generateOrder(Integer userId,List<OrderItem> orderItems,Integer shippingId){
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //订单总金额
        order.setPayment(getOrderTotalPrice(orderItems));
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(Consts.OrderStatumEnum.UNPAY.getStatus());
        order.setOrderNo(generateOrderNo());

        //将订单落库
        int count = orderMapper.insert(order);
        if (count <= 0){
            ServerResponse.serverResponseByFail(StatusEnum.ORDER_CREATE_FAIL.getStatus(),StatusEnum.ORDER_CREATE_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySuccess(null,order);
    }

    /***
     * 生成订单号
     */
    private long generateOrderNo(){
        return System.currentTimeMillis();
    }

    /**
     * 计算总金额
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice = new BigDecimal(0);
        for (OrderItem orderItem:orderItems){
            orderTotalPrice = BigDecimalUtils.add(String.valueOf(orderTotalPrice.doubleValue()),String.valueOf(orderItem.getCurrentUnitPrice()));
        }
        return orderTotalPrice;
    }

    private  ServerResponse assembleOrderItemList(List<Cart> cartList,Integer userId){

        List<OrderItem> orderItemList=new ArrayList<>();


        for(Cart cart:cartList){
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());

            orderItem.setUserId(userId);

            //根据商品id查询商品信息
            ServerResponse<ProductDetailVO>  serverResponse=iProductService.detail(cart.getProductId());
            if(!serverResponse.isSuccess()){
                return  serverResponse;
            }
            //商品是否处于在售状态
            ProductDetailVO productDetailVO=serverResponse.getData();
            if(productDetailVO.getStatus()!= Consts.ProductStatusEnum.PRODUCT_ONLINE.getStatus()){
                return  ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.PRODUCT_NOT_EXISTS.getDesc());
            }
            //判断商品库存是否充足
            if(productDetailVO.getStock()<cart.getQuantity()){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_STOCK_NOT_FULL.getStatus(),StatusEnum.PRODUCT_STOCK_NOT_FULL.getDesc());
            }

            orderItem.setProductId(cart.getProductId());
            orderItem.setCurrentUnitPrice(productDetailVO.getPrice());
            orderItem.setProductImage(productDetailVO.getMainImage());
            orderItem.setProductName(productDetailVO.getName());
            orderItem.setTotalPrice(BigDecimalUtils.multi(String.valueOf(productDetailVO.getPrice().doubleValue()),String.valueOf(cart.getQuantity())));

            orderItemList.add(orderItem);
        }
        return ServerResponse.serverResponseBySuccess(null,orderItemList);
    }

}
