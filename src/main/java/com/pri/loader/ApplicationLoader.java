package com.pri.loader;

import com.pri.annotation.*;
import com.pri.entities.ProxyEntity;
import com.pri.exception.ClassExitsException;
import com.pri.exception.NoInjectException;
import com.pri.factories.AopFactory;
import com.pri.factories.BeanFactory;
import com.pri.factories.MappingFactory;
import com.pri.proxy.ProxyInterceptor;
import net.sf.cglib.proxy.Enhancer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class ApplicationLoader {

    public static void load(String scanPackage) {
        Enumeration<URL> urlEnumeration = null;
        try {
            urlEnumeration = ApplicationLoader.class.getClassLoader().getResources(scanPackage.replace(".", "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (urlEnumeration.hasMoreElements()) {
            URL uri = urlEnumeration.nextElement();
            String fileUrl = uri.getFile();
            File directory = new File(fileUrl);

            initializeAop(directory, scanPackage);
            initializeBeans(directory, scanPackage);
            initializeFieldsInject();
        }
    }

    /**
     * 给每个实例下的相关字段注入实例
     */
    private static void initializeFieldsInject() {
        Iterator<Map.Entry<String, Object>> iterator = BeanFactory.getAll();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> map = iterator.next();
            Object instance = map.getValue();
            Field[] fields = instance.getClass().getSuperclass().getDeclaredFields();
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
    private static void initializeBeans(File directory, String packageName) {
        File[] files = directory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    initializeBeans(file, packageName + "." + file.getName());
                } else {
                    String fileName = file.getName();
                    Class clz = Class.forName(packageName + "." + fileName.replace(".class", ""));
                    if (isService(clz) || isController(clz)) {
                        System.out.println(packageName + "." + fileName.replace(".class", ""));
                        Object instance = Enhancer.create(clz, new ProxyInterceptor());
                        String className = fileName.replace(".class", "");
                        if(BeanFactory.isExits(className)){
                            throw new ClassExitsException();
                        }
                        BeanFactory.put(className, instance);
                        addMapping(clz);
                    }

                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassExitsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 包装所有web请求方法
     * @param controllerClz
     */
    private static void addMapping(Class controllerClz) {
        if (controllerClz.isAnnotationPresent(WebUrl.class)) {
            WebUrl webUrl = (WebUrl) controllerClz.getAnnotation(WebUrl.class);
            String path = webUrl.value();
            Method[] methods = controllerClz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(WebUrl.class)) {
                    WebUrl methodWebUrl =  method.getAnnotation(WebUrl.class);
                    path += methodWebUrl.value();
                    System.out.println(path);
                    MappingFactory.put(path, method);
                }
            }
        }
    }

    /**
     * 包装所有的AOP类
     * @param directory
     * @param packageName
     */
    private static void initializeAop(File directory, String packageName) {
        File[] files = directory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    initializeAop(file, packageName + "." + file.getName());
                } else {
                    String fileName = file.getName();
                    Class clz = Class.forName(packageName + "." + fileName.replace(".class", ""));
                    if (clz.isAnnotationPresent(Aop.class)) {
                        Field[] fields = clz.getFields();
                        Object instance = clz.newInstance();
                        ProxyEntity proxyEntity = new ProxyEntity();
                        proxyEntity.setInstance(instance);
                        proxyEntity.setTarget(clz);
                        proxyEntity.setClassName(clz.getSimpleName());
                        proxyEntity.setFullClassName(clz.getName());
                        for (Field field : fields) {
                            field.setAccessible(true);
                            Aop aop = field.getAnnotation(Aop.class);
                            if (null != aop) {
                                AopFactory.put(aop.value(), proxyEntity);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
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