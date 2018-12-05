package com.pri;

import com.alibaba.fastjson.JSONObject;
import com.pri.entities.MappingEntity;
import com.pri.factories.BeanFactory;
import com.pri.factories.MappingFactory;
import com.pri.loader.ApplicationLoader;
import com.pri.wrapper.RequestWrapper;
import com.pri.wrapper.ResponseWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        String scanPackagePath = config.getServletContext().getInitParameter("scanPackage");
        ApplicationLoader.load(scanPackagePath);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String mappingUri = req.getServletPath();

        MappingEntity mappingEntity = RequestWrapper.getMethod(mappingUri);
        if (null == mappingEntity) {
            resp.setStatus(404);
            resp.getWriter();
        } else {
            Method method = mappingEntity.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            Object clz = BeanFactory.get(className);
            try {
                Object result;
                if (method.getParameterCount() > 1) {
                    result = method.invoke(clz);
                } else if (method.getParameterCount() == 1) {
                    result = method.invoke(clz, "");
                } else {
                    result = method.invoke(clz);
                }
                String jsonRsult = JSONObject.toJSONString(result);
                ResponseWrapper.defaultResponse(resp, jsonRsult);
                resp.getWriter().print(jsonRsult);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }
}
