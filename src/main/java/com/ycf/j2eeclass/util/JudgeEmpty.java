package com.ycf.j2eeclass.util;

import java.lang.reflect.Field;

public class JudgeEmpty {
    /**
     * 利用反射判断元素中是否有null,过滤id
     * @param cls
     * @param obj
     * @return
     */
    public static String isBlank(Class<?> cls, Object obj) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object object = field.get(obj);
                if (object==null && !field.getName().equals("id")){
                    return field.getName() + "属性的值不能为空";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }

}
