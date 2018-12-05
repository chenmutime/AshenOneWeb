package com.pri.constant;

public enum HttpMethod {

    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private String name;

    HttpMethod(String name) {
        this.name = name;
    }
}
