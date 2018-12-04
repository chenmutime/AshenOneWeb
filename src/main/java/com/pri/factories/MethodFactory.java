package com.pri.factories;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodFactory {

    private static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    public static void put(String uri, Method method){
        methodMap.put(uri, method);
    }

    public static Method get(String uri){
        return methodMap.get(uri);
    }
}
