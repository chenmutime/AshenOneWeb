package com.pri.util;

import com.alibaba.fastjson.JSONObject;
import com.pri.annotation.RequestBody;
import com.pri.annotation.RequestParam;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class ParameterUtil {

    public static Object[] getParameterArray(HttpServletRequest req, Method method) throws IOException {
        List<Object> paramList = new LinkedList<>();
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    String value = req.getParameter(requestParam.name());
                    paramList.add(value);
                } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                    ServletInputStream servletInputStream = req.getInputStream();
                    String body = IOUtils.toString(servletInputStream, "utf-8");
                    Class paramType = parameter.getType();
                    Object bodyObj = JSONObject.parseObject(body, paramType);
                    paramList.add(bodyObj);
                } else {
                    paramList.add(null);
                }
            }

        }
        return paramList.toArray();
    }

}
