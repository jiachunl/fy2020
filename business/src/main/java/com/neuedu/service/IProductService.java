package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;

public interface IProductService {
    ServerResponse addOrUpdate(Product product);

    /**
     * 前台商品检索
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse list(Integer categoryId,String keyword, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 前台查看详情
     */
    ServerResponse detail(Integer productId);

    /**
     * 商品扣库存
     * type:0->减库存
     *      1->加库存
     */
    ServerResponse updateStock(Integer productId,Integer quantity,int type);



}
