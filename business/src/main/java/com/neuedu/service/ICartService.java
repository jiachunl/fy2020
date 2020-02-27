package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;

import java.util.List;

public interface ICartService {

    /**
     * 查看购物车列表
     */
    ServerResponse list(Integer userId);

    /**
     * 购物车中添加商品
     */

    ServerResponse add(Integer userId,Integer productId,Integer count);

    /**
     * 查询购物车中用户已经选中的商品
     */
    ServerResponse findCartByUserIdAndChecked(Integer userId);

    /**
     * 批量删除购物车商品
     */
    ServerResponse deleteBatchByIds(List<Cart> cartList);

}