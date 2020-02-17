package com.neuedu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse addOrUpdate(Product product) {
        if (product == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        Integer productId = product.getId();
        String subImages = product.getSubImages();//1.png,2.png,3.png
        if (subImages != null && subImages.length() > 0){
            String mainImage = subImages.split(",")[0];
            product.setMainImage(mainImage);
        }
        if (productId == null){
            //添加商品
            int insertCount = productMapper.insert(product);
            if (insertCount <= 0){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_ADD_FAIL.getStatus(),StatusEnum.PRODUCT_ADD_FAIL.getDesc());
            }else {
                return ServerResponse.serverResponseBySuccess("商品添加成功",null);
            }
        }else {
            //更新商品
            //1.查询商品
            Product product1 = productMapper.selectByPrimaryKey(product.getId());
            if (product1 == null){
                //更新的商品不存在
                return ServerResponse.serverResponseByFail(StatusEnum.UPDATE_PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.UPDATE_PRODUCT_NOT_EXISTS.getDesc());
            }

            //2.更新商品
            int updateCount = productMapper.updateProductByActivate(product);
            if (updateCount <= 0){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_UPDATE_FAIL.getStatus(),StatusEnum.PRODUCT_UPDATE_FAIL.getDesc());
            }else {
                return ServerResponse.serverResponseBySuccess("商品更新成功",null);
            }

        }
    }

    /**
     *
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     * 前台-商品搜索
     */
    @Override
    public ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //1.判断是否传递了categoryId和keyword
        if (categoryId == -1 && (keyword == null || keyword.equals(""))){
            //前端没有传递categoryId和keyword，往前端返回空的数据

            //PageHelper
            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(productList);
            return ServerResponse.serverResponseBySuccess(null,pageInfo);
        }

        //2.判断CategoryId是否传递
        List<Integer> categoryList = new ArrayList<>();
        if (categoryId != -1){
            //传递categoryId

            //查询categoryId下的所有子类
            ServerResponse<Set<Integer>> response = categoryService.get_deep_category(categoryId);
            if (response.isSuccess()){
                Set<Integer> categoryIds = response.getData();
                Iterator<Integer> iterator = categoryIds.iterator();
                while (iterator.hasNext()){
                    categoryList.add(iterator.next());
                }
            }
        }

        //3.判断keyword是否为空
        if (keyword != null && !keyword.equals("")){
            keyword = "%" + keyword + "%";
        }

        //4.执行查询

        //一定要写在前面
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.findProductsByCategoryIdsAndKeyword(categoryList,keyword);
        //构建分页模型
        PageInfo pageInfo = new PageInfo(productList);

        //5.返回结果
        return ServerResponse.serverResponseBySuccess("",pageInfo);
    }
}
