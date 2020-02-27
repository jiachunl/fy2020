package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    IShippingService iShippingService;
    @RequestMapping("/add.do")
    public ServerResponse add(Shipping shipping, HttpSession session){
        User user = (User) session.getAttribute(Consts.USER);
        shipping.setUserId(user.getId());
        return iShippingService.add(shipping);
    }
}
