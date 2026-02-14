package com.hpy.ops360.ticketing.logapi;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hpy.ops360.ticketing.cm.entity.ApiLog;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.util.TicketEventProducer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	@Autowired
	private TicketEventProducer ticketEventProducer;

	@Autowired
	private LoginService loginService;

	@Around("@annotation(Loggable)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
	    long start = System.currentTimeMillis();
	    try {
	        Object result = joinPoint.proceed();
	        double timeTaken = (System.currentTimeMillis() - start) / 1000.0;
	        writeLog(joinPoint, result, timeTaken, null); 
	        return result;
	    } catch (Exception e) {
	        double timeTaken = (System.currentTimeMillis() - start) / 1000.0;
	        writeLog(joinPoint, null, timeTaken, e);
	        throw e; 
	    }
	}

	
	
	
	private void writeLog(ProceedingJoinPoint joinPoint, Object result, double timeTakenInSeconds, Exception exception) {
	    String username = loginService.getLoggedInUser();
	    String className = joinPoint.getTarget().getClass().getSimpleName();
	    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	    String methodName = signature.getMethod().getName();

	    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
	    String controllerName = (request != null) ? request.getRequestURI() : "Unknown";

	    Object[] methodArgs = joinPoint.getArgs();
	    String requestData = Arrays.toString(methodArgs);

	    int statusCode = 0;
	    String responseBody = (result != null) ? result.toString() : "null";

	    if (result instanceof ResponseEntity<?> responseEntity) {
	        statusCode = responseEntity.getStatusCode().value();
	        Object body = responseEntity.getBody();
	        responseBody = (body != null) ? body.toString() : "null";
	    }

	    // If an exception occurred, log the error message instead of response
	    if (exception != null) {
	        responseBody = "Exception: " + exception.getMessage();
	        statusCode = 500; // Assuming it's a server error
	    }

	    ApiLog apiLog = new ApiLog();
	    apiLog.setUserName(username);
	    apiLog.setControllerName(controllerName);
	    apiLog.setClassName(className);
	    apiLog.setMethodName(methodName);
	    apiLog.setDateOfInsertion(LocalDateTime.now());
	    apiLog.setRequest(requestData);
	    apiLog.setResponse(responseBody);
	    apiLog.setStatusCode(statusCode);
	    apiLog.setTimeTaken(timeTakenInSeconds);

	    // Send log to Kafka
	    ticketEventProducer.sendMonitoring(apiLog);
	}


}