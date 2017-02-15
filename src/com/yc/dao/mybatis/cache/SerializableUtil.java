package com.yc.dao.mybatis.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//对要缓存的数据进行序列化（Object-->字节数组）以存入缓存和反序列化（字节数组-->Object）操作以从缓存中取出
public class SerializableUtil {
    //将对象序列化
    public static byte[] serialize(Object obj){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] bs = null;
        
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            
            oos.writeObject(obj);
            bs = baos.toByteArray();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }finally{
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bs;
    }
    
    //反序列化
    public static Object unSerialize(byte[] bs){
        ByteArrayInputStream bais=null;
        Object obj = null;
        
        try {
            bais= new ByteArrayInputStream(bs);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
