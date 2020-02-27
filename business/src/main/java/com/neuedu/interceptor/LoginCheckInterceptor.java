package com.neuedu.interceptor;

import com.google.gson.Gson;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    /**
     * 请求到达controller之前先通过preHandle
     * true：代表可以通过本拦截器，到达目标controller
     * false：拦截请求，返回到前端
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
//        System.out.println("==========preHandle========");
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        System.out.println(method.getName());
        HttpSession session = request.getSession();
        //判断用户是否登录
        User user = (User) session.getAttribute(Consts.USER);
        if (user != null){//用户已经登录
            return true;//可以通过拦截器
        }

        //重写Response
        PrintWriter printWriter = null;

        try {
            response.reset();//重置
            printWriter = response.getWriter();
            ServerResponse serverResponse = ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
            Gson gson = new Gson();
            String json = gson.toJson(serverResponse);

            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (printWriter != null){
                printWriter.close();
            }
        }
        return false;
    }

    /**
     * 当controller往前端响应时，通过拦截器的postHandle方法
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        System.out.println("=========postHandle===========");

    }

    /**
     * 一次http完成后，调用afterCompletion方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("==========afterCompletion===========");

    }
}
