package org.example.di;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

public class BeanFactoryUtils {
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        // Inject Annotation 붙은 파라미터만 가져옴
        Set<Constructor> injectedConstructors =
                ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));

        // 첫 번째 Constructor 가져오기
        if(injectedConstructors.isEmpty()){
            return null;
        } else {
            return injectedConstructors.iterator().next();
        }
    }
}
