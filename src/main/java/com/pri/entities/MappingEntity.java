package com.pri.entities;

import java.lang.reflect.Method;

public class MappingEntity {

    private String methodName;

    private String uriName;

    private String uriFullName;

    private Integer uriLength;

    private Integer uriFullLength;

    private String classPath;

    private Method method;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUriName() {
        return uriName;
    }

    public void setUriName(String uriName) {
        this.uriName = uriName;
    }

    public String getUriFullName() {
        return uriFullName;
    }

    public void setUriFullName(String uriFullName) {
        this.uriFullName = uriFullName;
    }

    public Integer getUriLength() {
        return uriLength;
    }

    public void setUriLength(Integer uriLength) {
        this.uriLength = uriLength;
    }

    public Integer getUriFullLength() {
        return uriFullLength;
    }

    public void setUriFullLength(Integer uriFullLength) {
        this.uriFullLength = uriFullLength;
    }
}
