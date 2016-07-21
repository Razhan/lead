package com.ran.delta.utils;

import java.util.HashMap;

final public class ClassUtils {

    private static final HashMap<Class, Class> PRIMITIVE_WRAPPERS = new HashMap<>();

    static {
        PRIMITIVE_WRAPPERS.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPERS.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPERS.put(Character.class, char.class);
        PRIMITIVE_WRAPPERS.put(Short.class, short.class);
        PRIMITIVE_WRAPPERS.put(Integer.class, int.class);
        PRIMITIVE_WRAPPERS.put(Long.class, long.class);
        PRIMITIVE_WRAPPERS.put(Float.class, float.class);
        PRIMITIVE_WRAPPERS.put(Double.class, double.class);
        PRIMITIVE_WRAPPERS.put(Void.class, void.class);
    }

    public static boolean canAssign(Class<?> baseClass, Class<?> classToCheck) {
        if (baseClass.isAssignableFrom(classToCheck)) {
            return true;
        } else {
            if (baseClass.isPrimitive()) {
                Class primitiveClass = PRIMITIVE_WRAPPERS.get(classToCheck);
                if (primitiveClass != null) {
                    return baseClass.isAssignableFrom(primitiveClass);
                }
            } else if (classToCheck.isPrimitive()) {
                Class primitiveClass = PRIMITIVE_WRAPPERS.get(baseClass);
                if (primitiveClass != null) {
                    return primitiveClass.isAssignableFrom(classToCheck);
                }
            }
        }

        return false;
    }
}
