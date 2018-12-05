package com.pri;

import com.alibaba.fastjson.JSONObject;
import com.pri.entities.MappingEntity;
import com.pri.factories.BeanFactory;
import com.pri.factories.MappingFactory;
import com.pri.loader.ApplicationLoader;
import com.pri.util.ParameterUtil;
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
        MappingEntity mappingEntity = MappingFactory.get(mappingUri);

        requestFilter(req, resp, mappingEntity);

        Method method = mappingEntity.getMethod();
        Object[] param = ParameterUtil.getParameter(req, method);
        String className = method.getDeclaringClass().getSimpleName();
        Object clz = BeanFactory.get(className);
        try {
            Object result;
            if (method.getParameterCount() > 0) {
                result = method.invoke(clz, param);
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

    private void requestFilter(HttpServletRequest req, HttpServletResponse resp, MappingEntity mappingEntity) throws IOException {
        if (null == mappingEntity) {
            resp.setStatus(404);
            resp.getWriter().print("page not found");
        } else if (!req.getMethod().toUpperCase().equals(mappingEntity.getHttpMethod())) {
            resp.setStatus(400);
            resp.getWriter().print("method not found");
        }
    }
}
