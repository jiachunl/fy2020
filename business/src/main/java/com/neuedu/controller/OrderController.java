package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;
    @RequestMapping("/create.do")
    public ServerResponse create(Integer shippingId, HttpSession session){
        //1.先判断用户是否登录
        User user = (User) session.getAttribute(Consts.USER);
        if (user == null){
//            未登录
            return ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
        }
        return orderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("/cancel.do")
    public ServerResponse cancel(Long orderNo){
        return orderService.cancelOrder(orderNo);
    }
}
