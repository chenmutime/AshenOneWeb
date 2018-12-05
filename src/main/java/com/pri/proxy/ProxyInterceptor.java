package com.pri.proxy;

import com.pri.annotation.AopAfter;
import com.pri.annotation.AopException;
import com.pri.annotation.AopBefore;
import com.pri.entities.ProxyEntity;
import com.pri.factories.AopFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProxyInterceptor implements MethodInterceptor {

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object object = null;
        Annotation[] annotations = method.getAnnotations();
        List<ProxyEntity> proxyEntityList = new ArrayList();
        for (Annotation annotation : annotations) {
            String annotationName = annotation.annotationType().getName();
            ProxyEntity proxyEntity = AopFactory.get(annotationName);
            if (null != proxyEntity) {
                proxyEntityList.add(proxyEntity);
            }
        }
        if (!proxyEntityList.isEmpty()) {
            for (ProxyEntity proxyEntity : proxyEntityList) {
                try {
                    doBefore(proxyEntity);
                    object = methodProxy.invokeSuper(o, args);
                    doAfter(proxyEntity);
                } catch (Exception e) {
                    doException(proxyEntity);
                }
            }
        } else {
            object = methodProxy.invokeSuper(o, args);
        }
        return object;
    }

    private void doBefore(ProxyEntity proxyEntity) throws InvocationTargetException, IllegalAccessException {
        Class clz = proxyEntity.getTarget();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AopBefore.class)) {
                method.invoke(proxyEntity.getInstance());
            }
        }
    }

    private void doAfter(ProxyEntity proxyEntity) throws InvocationTargetException, IllegalAccessException {
        Class clz = proxyEntity.getTarget();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AopAfter.class)) {
                method.invoke(proxyEntity.getInstance());
            }
        }
    }

    private void doException(ProxyEntity proxyEntity) throws InvocationTargetException, IllegalAccessException {
        Class clz = proxyEntity.getTarget();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AopException.class)) {
                method.invoke(proxyEntity.getInstance());
            }
        }
    }

}
