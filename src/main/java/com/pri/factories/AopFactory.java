package com.pri.factories;

import com.pri.entities.ProxyEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AopFactory {

    private static Map<String, ProxyEntity> aopMap = new ConcurrentHashMap<>();

    public static void put(String annotationName, ProxyEntity proxyEntity){
        aopMap.put(annotationName, proxyEntity);
    }

    public static ProxyEntity get(String annotationName){
        return aopMap.get(annotationName);
    }

}
