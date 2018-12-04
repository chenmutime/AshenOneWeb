package com.pri.proxy;

import com.pri.aspect.Aspect;
import com.pri.factories.AopFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ProxyFactory implements MethodInterceptor {

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object object = null;
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(">>>>>>>>>>>>>>"+annotation.annotationType().getName());
            String annotationName = annotation.annotationType().getName();
            Object aopInstance = AopFactory.get(annotationName);
            if(null != aopInstance){
                Aspect aspect = (Aspect) aopInstance;
                aspect.doBefore();
                object = methodProxy.invokeSuper(o, args);
                aspect.doAfter();
            }

        }
        if(null == object){
            object = methodProxy.invokeSuper(o, args);
        }
        return object;
    }

}
