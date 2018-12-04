package com.pri.wrapper;

import javax.servlet.http.HttpServletResponse;

public class ResponseWrapper {

    public static void defaultResponse(HttpServletResponse response, String content){
        response.setHeader("Content-Length",""+content.getBytes().length);
        response.setHeader("Content-Type","application/json");
    }

}
