package com.ef.newlead.util;

import com.ef.newlead.domain.usecase.UseCaseMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UseCaseUtils {

    public static Method findMethod(Object useCase, String methodName, Object... args) {
        List<Method> methods = getAnnotatedUseCaseMethods(useCase);

        if (methodName != null) {
            methods = findMethodByName(methods, methodName);

            if (methods.size() == 1) {
                return methods.get(0);
            }
        }

        if (args != null) {
            methods = findMethodByArgs(methods, args);

            if (methods.size() == 1) {
                return methods.get(0);
            } else {
                throw new IllegalArgumentException("check the annotated method");
            }
        }

        throw new IllegalArgumentException("check the annotated method");
    }

    private static List<Method> getAnnotatedUseCaseMethods(Object target) {
        List<Method> methods = new ArrayList<>();
        Method[] allMethods = target.getClass().getMethods();

        for (Method method : allMethods) {
            if (method.isAnnotationPresent(UseCaseMethod.class)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private static List<Method> findMethodByName(List<Method> list, String name) {
        if (list == null) {
            return null;
        }

        List<Method> methods = new ArrayList<>();

        for (Method method : list) {
            UseCaseMethod annotation = method.getAnnotation(UseCaseMethod.class);

            if (annotation.name().equals(name)) {
                methods.add(method);
            }
        }

        return methods;
    }

    private static List<Method> findMethodByArgs(List<Method> list, Object... args) {
        if (list == null) {
            return null;
        }

        List<Method> methods = new ArrayList<>();

        for (Method method : list) {
            Class<?>[] params = method.getParameterTypes();

            if (params.length != args.length) {
                continue;
            }

            boolean flag = true;
            for (int i = 0; i < params.length; i++) {
                if (!args[i].getClass().isAssignableFrom(params[i])) {
                    flag = false;
                }
            }

            if (flag) {
                methods.add(method);
            }
        }

        return methods;
    }
}
