package com.pri.factories;

import com.pri.entities.MappingEntity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MappingFactory {

    private static Map<String, MappingEntity> mappingMap = new ConcurrentHashMap<>();

    public static void put(String uri, MappingEntity method) {
        mappingMap.put(uri, method);
    }

    public static MappingEntity get(String uri) {
        return mappingMap.get(uri);
    }

    public static Set<String> getAllPath(){
        return mappingMap.keySet();
    }
}
