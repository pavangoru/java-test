package com.sharp.sharp.advice;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAdvice {

	Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

	@Pointcut(value = "execution(* com.sharp.sharp.*.*.*(..))")
	public void loggingPointCut() {

	}

	@Around("loggingPointCut()")
	public Object appLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String className = proceedingJoinPoint.getTarget().getClass().toString();
		String methodName = proceedingJoinPoint.getSignature().getName();
		Object[] args = proceedingJoinPoint.getArgs();
		log.info("Entering method {}::{}() with arguments {}", className, methodName, Arrays.toString(args));
		Object object = proceedingJoinPoint.proceed();
		log.info("Exiting from method {}::{}() with response {}", className, methodName, object);
		return object;
	}

	@AfterThrowing(pointcut = "loggingPointCut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exception in {}::{}() with error = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getMessage());
	}
}
