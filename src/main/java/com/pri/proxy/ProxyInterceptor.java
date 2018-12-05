package com.pri.proxy;

import com.pri.annotation.AopAround;
import com.pri.annotation.AopBefore;
import com.pri.annotation.AopException;
import com.pri.entities.ProxyClassEntity;
import com.pri.entities.ProxyPoint;
import com.pri.factories.AopFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProxyInterceptor implements MethodInterceptor {

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object object = null;
        Annotation[] annotations = method.getAnnotations();
        List<ProxyClassEntity> proxyClassEntityList = new ArrayList();
        for (Annotation annotation : annotations) {
            String annotationName = annotation.annotationType().getName();
//            这里proxyEntity指的是处理AOP的类，如HttpAspect类
            ProxyClassEntity proxyClassEntity = AopFactory.get(annotationName);
            if (null != proxyClassEntity) {
                proxyClassEntityList.add(proxyClassEntity);
            }
        }
        if (!proxyClassEntityList.isEmpty()) {
//            一个方法可能有多个AOP处理类
            for (ProxyClassEntity proxyClassEntity : proxyClassEntityList) {
                object = doProxy(proxyClassEntity, obj, method, args, methodProxy);
            }
        } else {
//            该方法没有可执行的AOP类
            object = methodProxy.invokeSuper(obj, args);
        }
        return object;
    }

    private Object doProxy(ProxyClassEntity proxyClassEntity, Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Map<String, Method> methodMap = proxyClassEntity.getMethodMap();
        Object object = null;
        try {
//            保存的通知方法中是否包含环绕通知
            if (methodMap.containsKey(AopAround.class.getName())) {
                ProxyPoint point = new ProxyPoint(method, obj, args, methodProxy);
                Method aroundMethod = methodMap.get(AopAround.class.getName());
                object = aroundMethod.invoke(proxyClassEntity.getInstance(), point);
            } else {
                Method beforeMethod = methodMap.get(AopBefore.class.getName());
                beforeMethod.invoke(proxyClassEntity.getInstance());
                methodProxy.invokeSuper(obj, args);
                Method afterMethod = methodMap.get(AopBefore.class.getName());
                afterMethod.invoke(proxyClassEntity.getInstance());
            }
        } catch (Exception e) {
            Method exceptionMethod = methodMap.get(AopException.class.getName());
            exceptionMethod.invoke(proxyClassEntity.getInstance(), e);
        }
        return object;
    }

}
