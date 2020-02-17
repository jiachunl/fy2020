package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;

import java.io.Serializable;

public interface IUserService {
    /**
     * 处理注册业务逻辑
     */
    public ServerResponse registerLogic(User user);

    /**
     * 处理登录业务逻辑
     */
    ServerResponse loginLogic(String username,String password);
}
