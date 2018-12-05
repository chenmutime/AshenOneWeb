package com.pri.wrapper;

import com.pri.entities.MappingEntity;
import com.pri.factories.MappingFactory;

import java.util.Set;

public class RequestWrapper {

    public static MappingEntity getMethod(String uri) {
        MappingEntity method = MappingFactory.get(uri);
        if (null == method) {
            String[] uriArr = uri.split("/");
            Set<String> pathSet = MappingFactory.getAllPath();
            for (String path : pathSet) {
                String[] pathArr = path.split("/");
                if (pathArr.length == uriArr.length) {
                    MappingEntity mappingEntity = MappingFactory.get(path);
                    if(uri.startsWith(mappingEntity.getClassPath())){
                        method = MappingFactory.get(path);
                        return method;
                    }
                }
            }
        }
        return method;
    }
}
