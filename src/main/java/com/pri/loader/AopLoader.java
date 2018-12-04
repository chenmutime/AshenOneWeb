package com.pri.loader;

import com.pri.annotation.Aop;
import com.pri.factories.AopFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;

public class AopLoader {

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
        }
    }

    private static void addInstances(File directory, String packageName) {
        File[] files = directory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    addInstances(file, packageName + "." + file.getName());
                } else {
                    String fileName = file.getName();
                    Class clz = Class.forName(packageName + "." + fileName.replace(".class", ""));
                    if (clz.isAnnotationPresent(Aop.class)) {
                        Field[] fields = clz.getFields();
                        Object instance = clz.newInstance();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            Aop aop = field.getAnnotation(Aop.class);
                            if (null != aop) {
                                AopFactory.put(aop.value(), instance);
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

}
