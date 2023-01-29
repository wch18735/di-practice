package org.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.example.di.BeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanFactoryTest {
    private Reflections reflections;
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp(){
        // Heap 메모리 인스턴스 중 org.example 하위 클래스 타입을 대상으로 함
        reflections = new Reflections("org.example");
        
        // Annotation(Controller, Service)을 전달해 해당 Annotation 이 붙은 대상을 조사함
        Set<Class<?>> preInstantiatedClasses = getTypesAnnotatedWith(Controller.class, Service.class);

        beanFactory = new BeanFactory(preInstantiatedClasses);
    }

    // Annotation 타입의 클래스들 개수는 미정임을 나타내는 "..."
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> ... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        
        // annotation 타입을 가진 객체를 모두 가져옴
        for(Class<? extends Annotation> annotation: annotations){
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }

    @Test
    void diTest() {
        UserController userController = beanFactory.getBean(UserController.class);
        assertThat(userController).isNotNull();
        assertThat(userController.getUserService()).isNotNull();
    }
}
