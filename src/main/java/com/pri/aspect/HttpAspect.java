package com.pri.aspect;

import com.pri.annotation.*;
import com.pri.entities.ProxyPoint;

/**
 * 如果方法中存在@AopAround，那么@AopBefore和@AopAfter便会失效
 */
@Aop
public class HttpAspect {

    //    拦截注解了HttpLog的方法
    @Aop("com.pri.annotation.HttpLog")
    public String annotationDefine;

    @AopBefore
    public void doBefore() {
        System.out.println("准备执行代码");
    }

    @AopAfter
    public void doAfter() {
        System.out.println("执行代码完成");
    }

//    @AopAround
//    public Object doAround(ProxyPoint point) throws Throwable {
//        System.out.println("准备执行代码222");
//        Object obj = point.process();
//        System.out.println("执行代码完成222");
//        return obj;
//    }

    @AopException
    public void doException(Exception e) {
        System.out.println("执行代码出现异常:" + e.getMessage());
    }

}
