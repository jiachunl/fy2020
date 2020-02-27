package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class PayController {

    @Autowired
    IPayService iPayService;
    @RequestMapping("/pay.do")
    public ServerResponse pay(Long orderNo){

        return iPayService.pay(orderNo);
    }
}
