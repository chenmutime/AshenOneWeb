package com.pri.entities;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ProxyPoint {

    private Method method;
    private Object instance;
    private MethodProxy methodProxy;
    private Object[] parameters;

    public ProxyPoint(Method method, Object instance, Object[] parameters, MethodProxy methodProxy) {
        this.method = method;
        this.instance = instance;
        this.parameters = parameters;
        this.methodProxy = methodProxy;
    }

    public Object process() throws Throwable {
        return methodProxy.invokeSuper(instance, parameters);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }
}
