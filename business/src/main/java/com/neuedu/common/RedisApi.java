package com.neuedu.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装redis中字符串、哈希、列表、集合、有序集合api
 * */

@Component
public class RedisApi {


    @Autowired
    private JedisPool jedisPool;


    /**
     * 字符串
     * 添加key,value
     *
     *
     * */
    public  String   set(String key,String value){
        Jedis jedis=jedisPool.getResource();
        String result=   jedis.set(key, value);
        jedis.close();

        return result;
    }

    /**
     * 字符串
     * 根据key获取value
     *
     *
     * */
    public  String   get(String key){
        Jedis jedis=jedisPool.getResource();
        String result=   jedis.get(key);
        jedis.close();

        return result;
    }


    /**
     * 字符串
     * key存在，设置不成功
     * key不存在，设置成功
     * */
    public Long setNx(String key,String value){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.setnx(key, value);
        jedis.close();
        return result;
    }

    /**
     * 原子性
     * 先get再set
     * */
    public  String  getSet(String key,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.getSet(key, value);
        jedis.close();
        return result;
    }

    /**
     * 为key设置过期时间
     *
     * */
    public Long  expire(String key,int timeout){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.expire(key, timeout);
        jedis.close();
        return result;

    }

    /**
     * 查看key剩余时间
     * */

    public Long  ttl(String key){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.ttl(key);
        jedis.close();
        return result;

    }

    /**
     * 在设置key,value时，为key指定过期时间
     * */

    public String setEx(String key,Integer timeout,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.setex(key,timeout,value);
        jedis.close();


        return result;
    }


    /**
     * 哈希结构-api封装
     * 设置key，field,value
     * */

    public Long  hset(String key,String field,String value){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.hset(key,field,value);
        jedis.close();
        return result;

    }
    /**
     * 哈希结构-api封装
     * 批量设置key，field,value
     * */

    public String  hset(String key,Map<String,String> map){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.hmset(key,map);
        jedis.close();
        return result;

    }

    /**
     * 哈希结构-api封装
     * 根据key，field查看value
     * */

    public String  hget(String key,String feild){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.hget(key,feild);
        jedis.close();
        return result;

    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的field、value
     * */

    public Map<String,String>  hgetAll(String key){
        Jedis jedis=jedisPool.getResource();
        Map<String,String> result=jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的feild
     * */

    public Set<String>  hgetAllField(String key){
        Jedis jedis=jedisPool.getResource();
        Set<String> result=jedis.hkeys(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的value
     * */

    public List<String> hgetAllVals(String key){
        Jedis jedis=jedisPool.getResource();
        List<String> result=jedis.hvals(key);
        jedis.close();
        return result;
    }


    /**
     * 哈希结构-api封装
     * 计数器
     * */

    public Long  hgetAllVals(String key,String field,Long incr){
        Jedis jedis=jedisPool.getResource();
        Long result=jedis.hincrBy(key,field,incr);
        jedis.close();
        return result;
    }



    /**
     * 发布消息
     * */

    public void pub(String channel,String message){

        Jedis jedis= jedisPool.getResource();
        System.out.println("  >>> fabu(PUBLISH) > Channel:"+channel+" > fa chu de Message:"+message);
        jedis.publish(channel, message);
        jedis.close();
    }

    /**
     * 订阅消息
     * */

    public void subscribe(JedisPubSub listener,String channel){
        Jedis jedis= jedisPool.getResource();
        jedis.subscribe(listener,channel);
        jedis.close();
    }

    /**
     * 取消订阅消息
     * */

    public void unsubscribe(JedisPubSub listener,String channel){
        Jedis jedis= jedisPool.getResource();
        System.out.println("  >>> qu xiao ding yue(UNSUBSCRIBE) > Channel:"+channel);
        listener.unsubscribe(channel);
        jedis.close();
    }







}