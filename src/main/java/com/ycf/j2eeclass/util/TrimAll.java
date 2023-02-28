package com.ycf.j2eeclass.util;

import java.lang.reflect.Field;

public class TrimAll {

    /**
     * 利用全反射去除参数的空格
     * @param cls
     * @param obj
     */
    public static void trim(Class<?> cls, Object obj) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object object = field.get(obj);
                if (object!=null && object.getClass() == String.class){
                    ((String) object).trim();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
