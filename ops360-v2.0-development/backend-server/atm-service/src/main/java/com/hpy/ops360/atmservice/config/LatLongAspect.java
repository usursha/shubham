package com.hpy.ops360.atmservice.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.UserLocation;
import com.hpy.ops360.atmservice.repository.UserLocationRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
@Aspect
@Component
@Slf4j
public class LatLongAspect {

    @Autowired
    private UserLocationRepository userLocationRepository;
    
    @Autowired
    private LoginService loginService;

    @Pointcut("execution(* *..controller..*.*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void beforeControllerMethod() {
        try {
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        	String latitudeHeader = request.getHeader("Latitude");
            String longitudeHeader = request.getHeader("Longitude");
            String username = loginService.getLoggedInUser();
                if (latitudeHeader != null && longitudeHeader != null) {
                	double latitude = Double.parseDouble(latitudeHeader);
                    double longitude = Double.parseDouble(longitudeHeader);
                    UserLocation userLocation = new UserLocation();
                    userLocation.setLatitude(latitude);
                    userLocation.setLongitude(longitude);
                    userLocation.setUsername(loginService.getLoggedInUser());
                    log.info("userLocation:"+userLocation);
                    userLocationRepository.save(userLocation);
                    log.info("Username: "+username+", Latitude: " + latitude + ", Longitude: " + longitude + " saved to database.");
                } else {
                    log.info("Lat & Long is null.");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
