package com.neuedu.controller;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;
    /**
     * 注册
     */
    @RequestMapping("/register.do")
    public ServerResponse register(User user){
        return userService.registerLogic(user);
    }

    /**
     * 登录接口
     */
    @RequestMapping("login.do")
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse response  =userService.loginLogic(username,password);
        if(response.isSuccess()){
            //登录成功
            session.setAttribute(Consts.USER,response.getData());
        }
        return response;
    }

    /**
     * 退出登录接口
     */
    @RequestMapping("logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Consts.USER);
        return ServerResponse.serverResponseBySuccess();
    }

    @Autowired
    UserMapper userMapper;



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

    @RequestMapping("/mybatis/{userid}")
    public ServerResponse findUser(@PathVariable("userid") int userid){
        User user = userMapper.selectByPrimaryKey(userid);
        if(user != null){
            return ServerResponse.serverResponseBySuccess(null,user);
        }
        return ServerResponse.serverResponseByFail(1,"id不存在");
    }
}
