package com.pri.factories;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingFactory {

    private static Map<String, Method> mappingMap = new ConcurrentHashMap<>();

    public static void put(String uri, Method method) {
        mappingMap.put(uri, method);
    }

    public static Method get(String uri) {
        return mappingMap.get(uri);
    }
}
