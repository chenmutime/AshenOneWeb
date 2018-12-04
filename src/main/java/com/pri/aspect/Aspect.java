package com.pri.aspect;

public interface Aspect {
    void doBefore();
    void doAfter();
    void doException(Exception e);
}
