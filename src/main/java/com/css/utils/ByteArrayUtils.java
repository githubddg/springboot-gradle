package com.css.utils;

import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

/**
 * @author jiangliubo
 * @version 1.0.0
 * @ClassName ByteArrayUtils
 * @Description TODO
 * @date 2020/03/18 15:23
 */
public class ByteArrayUtils {

    /**
     * object对象转二进制
     * @param obj
     * @param <T>
     * @return
     */
    public static<T>Optional<byte[]> objectToBytes(T obj){
        byte[] bytes;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(obj);
            oos.flush();
            bytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            bytes = new byte[0];
        }
        return Optional.ofNullable(bytes);
    }


    public static<T>Optional<T> bytesToObject(byte[] bytes){
        if (StringUtils.isEmpty(bytes)){
            return null;
        }
        T t = null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(in);
            t = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(t);
    }
}
