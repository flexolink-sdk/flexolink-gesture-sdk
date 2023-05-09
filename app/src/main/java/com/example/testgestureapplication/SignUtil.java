package com.example.testgestureapplication;


import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 签名工具类
 */
public class SignUtil {

    /**
     * 签名
     *
     * @param headers 头信息
     * @return 签名后的串
     */
    public static String sign(Map<String, Object> headers) {
        StringBuilder signBefore = new StringBuilder();
        // step1：先对请求参数排序
        TreeSet<String> sortSet = new TreeSet<>(headers.keySet());
        int i = 0;
        for (String key : sortSet) {
            i++;
            String value = String.valueOf(headers.get(key));
            signBefore.append(key).append("=").append(value);
            if (i != sortSet.size()) {
                signBefore.append("&");
            }
        }
        //md5加密
        String sign = md5(signBefore.toString());
        return sign;
    }

    /**
     * MD5加密
     *
     * @param content
     * @return
     */
    public static final String md5(String content) {
        char[] md5String = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = content.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 15];
                str[k++] = md5String[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }

    /**
     * 对象转map
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> map = new TreeMap<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Field f : fields) {
                f.setAccessible(true);
                Object val = f.get(obj);
                if (f.getType() == Date.class) {
                    map.put(f.getName(), sdf.format(val));
                } else {
                    map.put(f.getName(), val);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return map;
    }
}
