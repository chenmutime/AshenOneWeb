package com.pri;

import com.alibaba.fastjson.JSONObject;
import com.pri.factories.BeanFactory;
import com.pri.factories.MethodFactory;
import com.pri.loader.BeansLoader;
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
        System.out.println("项目启动");
        String scanPackagePath = config.getServletContext().getInitParameter("scanPackage");
        BeansLoader.load(scanPackagePath);

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String methodUri = req.getServletPath();

        Method method = MethodFactory.get(methodUri);
        String className = method.getDeclaringClass().getSimpleName();
        Object clz = BeanFactory.get(className);
        try {
            Object result = method.invoke(clz);
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
