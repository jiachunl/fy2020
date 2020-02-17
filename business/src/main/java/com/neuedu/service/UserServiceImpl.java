package com.neuedu.service;

import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse registerLogic(User user) {
        //1.参数非空判断
        if(user == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        //2.用户名非空判断
        if(user.getUsername() == null || user.getUsername().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        //3.邮箱非空判断
        if(user.getEmail() == null || user.getEmail().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_NOT_EMPTY.getStatus(),StatusEnum.EMAIL_NOT_EMPTY.getDesc());
        }

        //4.联系方式非空判断
        if(user.getPhone() == null || user.getPhone().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PHONE_NOT_EMPTY.getStatus(),StatusEnum.PHONE_NOT_EMPTY.getDesc());
        }

        //5.密保问题非空判断
        if(user.getQuestion() == null || user.getQuestion().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.QUESTION_NOT_EMPTY.getStatus(),StatusEnum.QUESTION_NOT_EMPTY.getDesc());
        }

        //6.密保问题答案非空判断
        if(user.getAnswer() == null || user.getAnswer().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.ANSWER_NOT_EMPTY.getStatus(),StatusEnum.ANSWER_NOT_EMPTY.getDesc());
        }

        //7.密码非空判断
        if(user.getPassword() == null || user.getPassword().equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }

        //判断用户名是否存在
        int count = userMapper.countUsername(user.getUsername());
        if(count > 0){
            //用户名存在
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_EXISTS.getStatus(),StatusEnum.USERNAME_EXISTS.getDesc());
        }

        //判断邮箱是否存在
        int countEmail = userMapper.countEmail(user.getEmail());
        if(countEmail > 0){
            //邮箱存在
            return ServerResponse.serverResponseByFail(StatusEnum.EMAIL_EXISTS.getStatus(),StatusEnum.EMAIL_EXISTS.getDesc());
        }

        //对明文的密码加密
        String password = MD5Utils.getMD5Code(user.getPassword());
        user.setPassword(password);

        //设置用户角色，默认普通用户
        user.setRole(RoleEnum.USER.getRole());
        int insertcount = userMapper.insert(user);
        if(insertcount == 0){
            //注册失败
            return ServerResponse.serverResponseByFail(StatusEnum.REGISTER_FAIL.getStatus(),StatusEnum.REGISTER_FAIL.getDesc());
        }

        return ServerResponse.serverResponseBySuccess("校验成功",null);
    }

    @Override
    public ServerResponse loginLogic(String username, String password) {
        /**
         * 参数非空判断
         */
        //用户名非空判断
        if(username == null || username.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EMPTY.getStatus(),StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }

        //密码非空判断
        if(password == null || password.equals("")){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }

        //判断用户名是否存在
        int count = userMapper.countUsername(username);
        if(count == 0){
            //用户名不存在
            return ServerResponse.serverResponseByFail(StatusEnum.USERNAME_NOT_EXISTS.getStatus(),StatusEnum.USERNAME_NOT_EXISTS.getDesc());
        }

        //对明文密码加密
        String _password = MD5Utils.getMD5Code(password);

        //根据用户名和密码查询用户信息
        User user = userMapper.findUserByUsernameAndPassword(username,_password);
        if(user == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PASSWORD_INCORRECT.getStatus(),StatusEnum.PASSWORD_INCORRECT.getDesc());
        }
        return ServerResponse.serverResponseBySuccess(null,user);
    }
}
