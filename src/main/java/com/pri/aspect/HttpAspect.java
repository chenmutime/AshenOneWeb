package com.pri.aspect;

import com.pri.annotation.Aop;

@Aop
public class HttpAspect implements Aspect{

    @Aop("com.pri.annotation.HttpLog")
    public String annotationDefine;

    @Override
    public void doBefore(){
        System.out.println("准备执行代码");
    }

    @Override
    public void doAfter() {
        System.out.println("执行代码完成");
    }
}
