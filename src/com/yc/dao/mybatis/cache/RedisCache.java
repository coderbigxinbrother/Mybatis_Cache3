package com.yc.dao.mybatis.cache;

import java.util.concurrent.locks.ReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.yc.dao.redis.RedisPool;

import redis.clients.jedis.Jedis;

public class RedisCache implements Cache {
    //日志
    private static Logger logger = (Logger) LogManager.getLogger(RedisCache.class);
    //id，必须存在，并且不能重复
    private String id;
    private Jedis redisClient=createRedis();
    private ReadWriteLock readWriteLock=null;
    
    public RedisCache(String id){
        if(id == null){
            throw new IllegalArgumentException("Cache instance reqirse an ID");
        }
        logger.debug("Cache an cache instance with ID:" + id);
        this.id = id;
    }
    @Override//将缓存中的数据删除
    public void clear() {
        this.redisClient.flushDB();
    }

    @Override//获取id
    public String getId() {
        return this.id;
    }

    @Override//通过key到缓存redis中取值
    public Object getObject(Object key) {
        //缓存穿透--》访问了缓存中没有的数据
        byte[] values = this.redisClient.get(SerializableUtil.serialize(key));
        if(values == null){
            //1、返回 null
            return null;
            //2、存入null值
            //this.putObject(SerializableUtil.serialize(key), null);
        }
        Object obj = SerializableUtil.unSerialize(values);
        return obj;
    }

    @Override//获取读写锁
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override//获取缓存的大小
    public int getSize() {
        Long size = this.redisClient.dbSize();
        int s = Integer.valueOf(size+"");
        return s;
    }

    @Override//将数据存入缓存
    public void putObject(Object key, Object value) {
        //对将要存的数据进行序列化形成字节数组
        byte[] keyByte = SerializableUtil.serialize(key);
        byte[] valueByte = SerializableUtil.serialize(value);
        this.redisClient.set(keyByte,valueByte);
    }

    @Override
    public Object removeObject(Object key) {
        //从缓存移除数据
        byte[] keyByte = SerializableUtil.serialize(key);
        Long result = this.redisClient.expire(keyByte,0);
        return result;
    }
    
    //创建redis
    protected static Jedis createRedis() {
        //TODO：获取jedis实例
        //Jedis jedis = new Jedis("192.168.43.137");
        Jedis jedis = RedisPool.getPool().getResource();
        return jedis;
    }
}
