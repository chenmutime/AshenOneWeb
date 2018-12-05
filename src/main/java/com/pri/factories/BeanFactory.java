package com.pri.factories;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

//    存的是实例，原本存的是class，然后这样就无法依赖注入字段了
    private static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    public static void put(String className, Object clz){
        beanMap.put(className, clz);
    }

    public static Object get(String className){
        return beanMap.get(className);
    }

    public static Iterator<Map.Entry<String, Object>> getAll(){
        return beanMap.entrySet().iterator();
    }

    public static boolean isExits(String className){
        return null != beanMap.get(className);
    }

}
