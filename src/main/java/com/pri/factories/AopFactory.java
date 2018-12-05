package com.pri.factories;

import com.pri.entities.ProxyClassEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AopFactory {

    private static Map<String, ProxyClassEntity> aopMap = new ConcurrentHashMap<>();

    public static void put(String annotationName, ProxyClassEntity proxyClassEntity){
        aopMap.put(annotationName, proxyClassEntity);
    }

    public static ProxyClassEntity get(String annotationName){
        return aopMap.get(annotationName);
    }

}
