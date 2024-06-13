package com.todo.api.logging;

import com.todo.api.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("execution(* com.todo.api..impl.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("====== method name : {} ======", methodName);

        for (Object arg : args) {
            log.info("parameter : {}", arg);
        }
    }

    @AfterReturning(pointcut = "execution(* com.todo.api..impl.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("====== successfully ======");
    }

    @AfterThrowing(pointcut = "execution(* com.todo.api..impl.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            log.warn("CustomException in method");
            log.warn("code : {}", customException.getCode());
            log.warn("message : {}", customException.getMessage());
        } else {
            log.error("Exception in method");
            log.error("cause : {}", e.getCause());
            log.error("message : {}", e.getMessage());
        }
        log.info("==========================");
    }

}
