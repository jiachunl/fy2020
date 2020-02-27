package com.neuedu.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {

    private boolean allChecked;//是否全选
    private BigDecimal cartTotalPrice;//购物车总价格

    private List<CartProductVo> cartProductVOList;

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public List<CartProductVo> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVo> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }
}