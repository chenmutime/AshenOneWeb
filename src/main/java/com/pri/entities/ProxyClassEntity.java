package com.pri.entities;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理AOP的类，如HttpAspect
 */
public class ProxyClassEntity {

    private String className;

    private String fullClassName;

    private Map<String, Method> methodMap;

    private Class target;

    private Object instance;

    public synchronized Map<String, Method> getMethodMap() {
        if(null == methodMap){
            methodMap = new HashMap<>();
        }
        return methodMap;
    }

    public void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }
}
