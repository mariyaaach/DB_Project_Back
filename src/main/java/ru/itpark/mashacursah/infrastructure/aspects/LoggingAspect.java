package ru.itpark.mashacursah.infrastructure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation( ru.itpark.mashacursah.infrastructure.aspects.Log) || @within( ru.itpark.mashacursah.infrastructure.aspects.Log)")
    public void logAnnotationPointcut() {
        // Pointcut для методов и классов, помеченных @Log
    }

    @Before("logAnnotationPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String args = String.join(", ", getArgsAsString(joinPoint.getArgs()));

        logger.info("Вход в метод [{}::{}] с аргументами [{}]", className, methodName, args);
    }

    @AfterReturning(pointcut = "logAnnotationPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        logger.info("Метод [{}::{}] вернул [{}]", className, methodName, result);
    }

    @AfterThrowing(pointcut = "logAnnotationPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        logger.error("Метод [{}::{}] выбросил исключение", className, methodName, exception);
    }

    @After("logAnnotationPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        logger.info("Выход из метода [{}::{}]", className, methodName);
    }

    private String[] getArgsAsString(Object[] args) {
        String[] argsString = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            argsString[i] = String.valueOf(args[i]);
        }
        return argsString;
    }
}