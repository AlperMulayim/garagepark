package com.alper.garageparkapi.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class GarageParkLogger {
    private static  final String POINT_CUT = "within(com.alper.garageparkapi..*)";

    @Around(POINT_CUT)
    public Object logExec(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        Object proceed = null;

        try {
            Signature signature = proceedingJoinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            proceed =  proceedingJoinPoint.proceed();
            if(proceed != null){
                System.out.println("[ METHOD_EXECUTED ]-- " + method  + "  -- [ METHOD_RESULT ]-- " + proceed.toString());
            }else {
                System.out.println("[ METHOD_EXECUTED ]-- " + method);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return proceed;
    }

}
