package com.yc.dao.redis;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool{
    private static  JedisPool pool;
    private RedisPool(){
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        
      //jedis池化配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Integer.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.maxOnReturn")));


        //创建连接
        pool = new JedisPool(config,bundle.getString("redis.ip"),Integer.valueOf(bundle.getString("redis.port")));
    }
    
    public synchronized static JedisPool getPool(){
        if(pool == null){
            new RedisPool();
        }
        return pool;
    }
}
