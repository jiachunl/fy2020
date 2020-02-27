package com.neuedu;

import com.neuedu.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        List<String> loginPath = new ArrayList<>();

        loginPath.add("/manager/**");
        loginPath.add("/user/**");
        loginPath.add("/cart/**");
        loginPath.add("/order/**");
        loginPath.add("/shipping/**");

        List<String> excludePath = new ArrayList<>();

        excludePath.add("/user/login.do");
        excludePath.add("/user/register.do");
        excludePath.add("/user/forget_get_question.do");
        excludePath.add("/manager/user/login.do");

        registry.addInterceptor(loginCheckInterceptor).//注册拦截器
                addPathPatterns(loginPath).//需要添加拦截的路径
                excludePathPatterns(excludePath);

    }
}
