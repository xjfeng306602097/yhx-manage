package com.yhx.common.util;

import java.io.*;

/**
 * 序列化与反序列化
 * @author gaolinlou
 * @time 2016年4月7日 下午4:00:41
 */
public class SerializeUtil {
	
	/**
	 * 序列化操作
	 * 功能详细描述
	 * @param  object 需要序列化的对象
	 * @return byte[]	序列化返回的字节数组
	 */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            byte[] b = bos.toByteArray();
            return b;
        } catch (IOException e) {
            System.out.println("序列化失败 Exception:" + e.toString());
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                System.out.println("io could not close:" + ex.toString());
            }
        }
    }
    
	/**
	 * 反序列化
	 * @param  bytes 序列化的字节数组
	 * @return Object	[返回类型说明]
	 */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("bytes Could not deserialize:" + e.toString());
            return null;
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException ex) {
                System.out.println("LogManage Could not serialize:" + ex.toString());
            }
        }
    }
}
