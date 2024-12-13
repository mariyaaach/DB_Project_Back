package ru.itpark.mashacursah.infrastructure.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    LogLevel level() default LogLevel.INFO;
    boolean logArgs() default true;
    boolean logResult() default true;
}