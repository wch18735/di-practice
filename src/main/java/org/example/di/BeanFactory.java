package org.example.di;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private final Set<Class<?>> preInstantiatedClasses;

    // All beans are singleton, so Map data structure would be appropriate
    // Key: class type
    // Value: class instance
    private Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> preInstantiatedClasses) {
        this.preInstantiatedClasses = preInstantiatedClasses;
        initialize();
    }

    private void initialize() {
        for(Class<?> clazz : preInstantiatedClasses){
            Object instance = createInstance(clazz);
            beans.put(clazz, instance);
        }
    }

    // Class<?> means that undefined class type
    // so, Class<?> sourced from (***).class
    private Object createInstance(Class<?> clazz) {
        // undefined class type object constructor
        Constructor<?> constructor = findConstructor(clazz);

        // constructor parameter array list
        List<Object> parameters = new ArrayList<>();
        for(Class<?> typeClass : constructor.getParameterTypes()){
            // get parameter class type in constructor
            // ex: User(String name, Integer age) then String, Integer
            parameters.add(getParameterByClass(typeClass));
        }

        // instance check
        try {
            // return instance with parameters
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getParameterByClass(Class<?> typeClass) {
        Object instanceBean = getBean(typeClass);
        if(Objects.nonNull(instanceBean)){
            return instanceBean;
        }
        return createInstance(typeClass);
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        return getConstructor(clazz);
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if(Objects.nonNull(injectedConstructor)){
            return injectedConstructor;
        }

        return clazz.getConstructors()[0];
    }

    // T would be instance, otherwise Class<T> would be class type
    // [access controller] <generic type> [return type] [method name] ([generic type] [parameter])
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }
}
