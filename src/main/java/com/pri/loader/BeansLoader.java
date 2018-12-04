package com.pri.loader;

import com.pri.annotation.Controller;
import com.pri.annotation.Inject;
import com.pri.annotation.Service;
import com.pri.annotation.WebUrl;
import com.pri.exception.NoInjectException;
import com.pri.factories.BeanFactory;
import com.pri.factories.MethodFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class BeansLoader {

    public static void load(String scanPackage) {
        Enumeration<URL> urlEnumeration = null;
        try {
            urlEnumeration = BeansLoader.class.getClassLoader().getResources(scanPackage.replace(".", "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (urlEnumeration.hasMoreElements()) {
            URL uri = urlEnumeration.nextElement();
            String fileUrl = uri.getFile();
            File directory = new File(fileUrl);
            addInstances(directory, scanPackage);
            depencyInject();
        }
    }

    /**
     * 给每个实例下的相关字段注入示例
     */
    private static void depencyInject() {
        Iterator<Map.Entry<String, Object>> iterator = BeanFactory.getAll();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> map = iterator.next();
            Object instance = map.getValue();
//          执行依赖注入
            Field[] fields = instance.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Inject.class)) {
                        Object subInstance = BeanFactory.get(field.getType().getSimpleName());
                        if (null == subInstance) {
                            throw new NoInjectException();
                        }
                        field.set(instance, subInstance);
                    }
                }
            } catch (NoInjectException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 遍历指定扫描包下注解了@Conroller的类加载到BeanFactory，并加载所有注解了@WebUrl的方法到MethodFactory
     *
     * @param directory
     * @param packageName
     */
    private static void addInstances(File directory, String packageName) {
        File[] files = directory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    addInstances(file, packageName + "." + file.getName());
                } else {
                    String fileName = file.getName();
                    Class clz = Class.forName(packageName + "." + fileName.replace(".class", ""));
                    if (isService(clz) || isController(clz)) {
                        System.out.println(packageName + "." + fileName.replace(".class", ""));
                        BeanFactory.put(fileName.replace(".class", ""), clz.newInstance());
                        String path = "";
                        if (clz.isAnnotationPresent(WebUrl.class)) {
                            WebUrl webUrl = (WebUrl) clz.getAnnotation(WebUrl.class);
                            path = webUrl.value();
                        }
                        Method[] methods = clz.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(WebUrl.class)) {
                                WebUrl webUrl = method.getAnnotation(WebUrl.class);
                                path += webUrl.value();
                                System.out.println(path);
                                MethodFactory.put(path, method);
                            }
                        }
                    }

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static boolean isService(Class clz) {
        return clz.isAnnotationPresent(Service.class);
    }

    private static boolean isController(Class clz) {
        return clz.isAnnotationPresent(Controller.class);
    }
}
