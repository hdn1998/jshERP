package com.jsh.erp.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
//@Aspect
public class RecordTimeAspect {

    @Around("execution(* com.jsh.erp.service.*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("方法 {} 执行耗时：{}ms", joinPoint.getSignature(), endTime - startTime);
        return result;
    }
}
