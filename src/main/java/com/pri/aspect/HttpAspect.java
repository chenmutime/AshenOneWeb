package com.pri.aspect;

import com.pri.annotation.AopAfter;
import com.pri.annotation.Aop;
import com.pri.annotation.AopBefore;

@Aop
public class HttpAspect {

//    拦截注解了HttpLog的方法
    @Aop("com.pri.annotation.HttpLog")
    public String annotationDefine;

    @AopBefore
    public void doBefore(){
        System.out.println("准备执行代码");
    }

    @AopAfter
    public void doAfter() {
        System.out.println("执行代码完成");
    }

    public void doException(Exception e) {
        System.out.println("执行代码出现异常:"+e.getMessage());
    }

}
