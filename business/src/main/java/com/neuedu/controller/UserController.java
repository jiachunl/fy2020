package com.neuedu.controller;

import com.neuedu.pojo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    /*读取配置文件中的内容*/
    @Value("${user.username}")
    private String username;

    @RequestMapping("/user")
    public User findUserById(@RequestParam("id") Integer userid){
        User user = new User();
        user.setId(userid);
        user.setUsername(username);
        return user;
    }

    @RequestMapping("/user/{id}/{username}")
    public User findUserById2(@PathVariable("id") Integer userid,@PathVariable("username") String username){
        User user = new User();
        user.setId(userid);
        user.setUsername(username);
        return user;
    }
}
