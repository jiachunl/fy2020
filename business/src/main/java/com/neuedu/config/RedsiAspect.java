package com.neuedu.config;

import com.google.gson.Gson;
import com.neuedu.common.RedisApi;
import com.neuedu.common.ServerResponse;
import com.neuedu.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect //声明切面类
@Component
public class RedsiAspect {


    @Autowired
    RedisApi redisApi;


    //step1: 定义切入点 execution:切入点表达式
    @Pointcut("execution(public * com.neuedu.service.CategorySereviceImpl.get*(..))")
    public  void test(){}


    //step2:定义通知

//    //前置通知
//    @Before("test()")
//    public void  before(){
//
//        System.out.println("===========before=======");
//    }
//
//    //后置通知
//    @After("test()")
//    public void  after(){
//
//        System.out.println("===========after=======");
//    }
//
//    //返回后通知
//    @AfterReturning("test()")
//    public void  afterReturning(){
//
//        System.out.println("===========AfterReturning=======");
//    }
//
//
//    //抛出异常后通知
//    @AfterThrowing("test()")
//    public void  afterThrowing(){
//
//        System.out.println("===========afterThrowing=======");
//    }


    //环绕通知

    @Around("test()")
    public  Object  around(ProceedingJoinPoint joinPoint){

        StringBuffer stringBuffer=new StringBuffer();

        //类名
        String className=joinPoint.getSignature().getDeclaringType().getName();

        stringBuffer.append(className);
        String name=joinPoint.getSignature().getName();//方法名
        stringBuffer.append(name);
        Object[] args= joinPoint.getArgs();
        for(Object o:args){//参数值数组
            stringBuffer.append(o);
        }


        // 缓存key
        String  cacheKey=MD5Utils.getMD5Code(stringBuffer.toString());

        System.out.println(cacheKey);
        //从缓存中读取数据
        String cacheValue=redisApi.get(cacheKey);
        Gson gson=new Gson();
        if(cacheValue==null){//缓存中没有数据


            try {
                Object o= joinPoint.proceed();//执行目标方法,读取db


                String valuestr=gson.toJson(o);
                //写入缓存
                redisApi.set(cacheKey,valuestr);
                return o;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }



        }else{//缓存有数据

            ServerResponse serverResponse=gson.fromJson(cacheValue,ServerResponse.class);
            return serverResponse;
        }

        return null;

    }




}
