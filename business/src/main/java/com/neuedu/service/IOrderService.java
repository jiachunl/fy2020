package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IOrderService {
    /**
     * 创建订单
     */
    ServerResponse createOrder(Integer userId,Integer shippingId);

    /**
     * 取消订单
     */
    ServerResponse cancelOrder(Long orderNo);

    ServerResponse findOrderByOrderNo(Long orderNo);
}
