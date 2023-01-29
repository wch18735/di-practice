package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // annotation target type
@Retention(RetentionPolicy.RUNTIME) // retention policy
public @interface Controller {

}
