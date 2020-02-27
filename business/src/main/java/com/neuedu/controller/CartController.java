package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    /**
     * 购物车列表
     * */

    @RequestMapping("/list.do")
    public ServerResponse list(HttpSession session){

        //step1:先判断用户是否登录
        User user=(User)session.getAttribute(Consts.USER);
        if(user==null){//未登录
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }

        //step2:



        return cartService.list(user.getId());

    }

    @RequestMapping("/add.do")
    public ServerResponse add(Integer productId,Integer count,HttpSession session){

        //step1:先判断用户是否登录
        User user=(User)session.getAttribute(Consts.USER);
        if(user==null){//未登录
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }

        return cartService.add(user.getId(),productId,count);

    }

}
