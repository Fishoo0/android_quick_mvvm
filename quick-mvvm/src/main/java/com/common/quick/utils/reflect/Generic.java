package com.common.quick.utils.reflect;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generic {

    public static <T> Class<T> getParamType(Class<?> clazz, int index) {
        Type superclass = clazz.getGenericSuperclass();

        while (superclass instanceof Class) {
            superclass = ((Class) superclass).getGenericSuperclass();
        }

        if (superclass == null) {
            throw new RuntimeException("Missing type parameter.");
        }

        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getActualTypeArguments()[index];
        if (type instanceof ParameterizedType)
            return (Class<T>) ((ParameterizedType) type).getRawType();
        else
            return (Class<T>) type;

    }

    public static <T> Class<T> getParamTypeForSuper(Class<?> clazz, int index) {
        while (clazz != null) {
            Type superclass = clazz.getGenericSuperclass();
            if (superclass instanceof Class) {
//                throw new RuntimeException("Missing type parameter.");
                clazz = clazz.getSuperclass();
                continue;
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            Type type = parameterized.getActualTypeArguments()[index];
            if (type instanceof ParameterizedType)
                return (Class<T>) ((ParameterizedType) type).getRawType();
            else
                return (Class<T>) type;
        }

        throw new RuntimeException("Missing type parameter.");
    }

}
