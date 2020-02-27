package com.neuedu.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class JedisConfig {



    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.maxIdle}")
    private Integer  maxIdle;
    @Value("${redis.minIdle}")
    private Integer  minIdle;
    @Value("${redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.jmxEnabled}")
    private boolean jmxEnabled;

    @Value("${redis.redisHost}")
    private String redisHost;
    @Value("${redis.redisPort}")
    private Integer redisPort;
    @Value("${redis.redisPassword}")
    private String redisPass;
    @Value("${redis.timeout}")
    private Integer timeout;


    @Bean
    public  GenericObjectPoolConfig genericObjectPoolConfig(){

        GenericObjectPoolConfig config=new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setJmxEnabled(jmxEnabled);
        return config;

    }

    /**
     *
     * JedisPool的初始化，交给Ioc管理
     * */


    @Bean
    public JedisPool jedisPool(){
        JedisPool jedisPool=new JedisPool(genericObjectPoolConfig(),redisHost,redisPort,timeout,redisPass);
        return jedisPool;
    }



}
