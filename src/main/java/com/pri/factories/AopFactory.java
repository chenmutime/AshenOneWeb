package com.pri.factories;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AopFactory {

    private static Map<String, Object> aopMap = new ConcurrentHashMap<>();

    public static void put(String annotationName, Object clz){
        aopMap.put(annotationName, clz);
    }

    public static Object get(String annotationName){
        return aopMap.get(annotationName);
    }

}
