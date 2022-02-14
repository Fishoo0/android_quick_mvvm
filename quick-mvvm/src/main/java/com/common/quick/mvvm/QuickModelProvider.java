package com.common.quick.mvvm;


import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * TalModel Manager,使用@Instance，表示Model需要单例存在，否则直接创建
 */
public class QuickModelProvider {

    private static Map<Class, QuickBaseModel> sTalModelMap = new HashMap<>();

    public static <T extends QuickBaseModel> T getModel(Class<T> t, Application context) {
        try {
            if (t.isAnnotationPresent(Instance.class)) {
                if (sTalModelMap.containsKey(t)) {
                    return (T) sTalModelMap.get(t);
                }
                T model = t.getConstructor(Context.class).newInstance(context);
                sTalModelMap.put(t.getClass(), model);
                return model;
            }
            return t.getConstructor(Application.class).newInstance(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void clear() {
        sTalModelMap.clear();
    }

    /**
     * 释放某个指定的model
     */
    public static void clear(Class cl) {
        if (sTalModelMap.containsKey(cl)) {
            sTalModelMap.remove(cl);
        }
    }

}
