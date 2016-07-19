package com.ran.delta.domain.usecase;

import com.ran.delta.domain.annotation.UseCaseFunction;
import com.ran.delta.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class UseCaseFilter {

    static Method filter(Object useCase, String useCaseName, Object[] args) {
        List<Method> methodsFiltered = getAnnotatedUseCaseMethods(useCase);
        if (methodsFiltered.isEmpty()) {
            throw new IllegalArgumentException("This object doesn't contain any use case to execute."
                    + "Did you forget to add the @UseCaseFunction annotation?");
        } else if (methodsFiltered.size() == 1) {
            return methodsFiltered.get(0);
        }

        methodsFiltered = getMethodMatchingName(useCaseName, methodsFiltered);
        if (methodsFiltered.isEmpty()) {
            throw new IllegalArgumentException("The target doesn't contain any use case with this name."
                    + "Did you forget to add the @UseCaseFunction annotation?");
        } else if (methodsFiltered.size() == 1) {
            return methodsFiltered.get(0);
        }

        methodsFiltered = getMethodMatchingArguments(args, methodsFiltered);
        if (methodsFiltered.isEmpty()) {
            throw new IllegalArgumentException("The target doesn't contain any use case with those args. "
                    + "Did you forget to add the @UseCaseFunction annotation?");
        }

        if (methodsFiltered.size() > 1) {
            throw new IllegalArgumentException(
                    "The target contains more than one use case with the same signature. "
                            + "You can use the 'name' property in @UseCaseFunction and invoke it with a param name");
        }

        return methodsFiltered.get(0);
    }


    private static List<Method> getAnnotatedUseCaseMethods(Object target) {
        List<Method> useCaseMethods = new ArrayList<>();

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            UseCaseFunction useCaseMethodMethod = method.getAnnotation(UseCaseFunction.class);

            if (useCaseMethodMethod != null) {
                useCaseMethods.add(method);
            }
        }
        return useCaseMethods;
    }

    private static List<Method> getMethodMatchingArguments(Object[] selectedArgs,
                                                           List<Method> methodsFiltered) {
        if (selectedArgs == null || selectedArgs.length < 1) {
            return methodsFiltered;
        }

        Iterator<Method> iteratorMethods = methodsFiltered.iterator();

        while (iteratorMethods.hasNext()) {
            Method method = iteratorMethods.next();

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == selectedArgs.length) {
                if (!hasValidArguments(parameters, selectedArgs)) {
                    iteratorMethods.remove();
                }
            } else {
                iteratorMethods.remove();
            }
        }

        return methodsFiltered;
    }

    private static boolean hasValidArguments(Class<?>[] parameters, Object[] selectedArgs) {
        for (int i = 0; i < parameters.length; i++) {
            Object argument = selectedArgs[i];
            if (argument != null) {
                Class<?> targetClass = argument.getClass();
                Class<?> parameterClass = parameters[i];
                if (!ClassUtils.canAssign(parameterClass, targetClass)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Method> getMethodMatchingName(String nameUseCase,
                                                      List<Method> methodsFiltered) {
        if (nameUseCase == null || nameUseCase.equals("")) {
            return methodsFiltered;
        }

        Iterator<Method> iteratorMethods = methodsFiltered.iterator();
        while (iteratorMethods.hasNext()) {
            Method method = iteratorMethods.next();
            UseCaseFunction annotation = method.getAnnotation(UseCaseFunction.class);
            if (!(annotation.name().equals(nameUseCase))) {
                iteratorMethods.remove();
            }
        }

        return methodsFiltered;
    }
}
