//package com.hpy.ops360.dashboard.config;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.hpy.ops360.dashboard.location.entity.UserLocation;
//import com.hpy.ops360.dashboard.repository.UserLocationRepository;
//import com.hpy.ops360.dashboard.service.LoginService;
//import com.hpy.ops360.dashboard.util.Helper;
//import com.hpy.ops360.framework.dto.PointDto;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//
//import java.lang.reflect.Field;
//
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//@Aspect
//@Component
//@Slf4j
//@Order(Ordered.LOWEST_PRECEDENCE)
//public class LatLongAspect {
//
//    @Autowired
//    private UserLocationRepository userLocationRepository;
//    
//    @Autowired
//    private Helper loginService;
//    
////    @Autowired
////    private HttpServletRequest request;
//
////    @Pointcut("execution(* com.hpy.ops360.ticketing.controller.*.*(..)) && args(dto,..)")
//    @Pointcut("execution(* com.hpy.ops360.dashboard.controller..*.*(..))")
//    public void controllerMethods() {}
//
////    @Before("controllerMethods(dto)")
////    public void beforeControllerMethod(Object dto) {
////        try {
////            Field pointField = findField(dto.getClass(), "point");
////            Field usernameField = findField(dto.getClass(), "username");
////            if (pointField != null) {
////                pointField.setAccessible(true);
////                usernameField.setAccessible(true);
////                PointDto point = (PointDto) pointField.get(dto);
////                String username = (String) usernameField.get(dto);
////                if (point != null) {
////                    double latitude = point.getLatitude();
////                    double longitude = point.getLongitude();
//////                    UserLocation userLocation = UserLocation.builder().latitude(latitude).longitude(longitude).username(username).build();
////                    UserLocation userLocation = new UserLocation();
////                    userLocation.setLatitude(latitude);
////                    userLocation.setLongitude(longitude);
////                    userLocation.setUsername(loginService.getLoggedInUser());
////                    System.out.println("userLocation:"+userLocation);
////                    userLocationRepository.save(userLocation);
////                    System.out.println("Username: "+username+", Latitude: " + latitude + ", Longitude: " + longitude + " saved to database.");
////                } else {
////                    System.out.println("PointDto is null.");
////                }
////            } else {
////                System.out.println("No PointDto field found.");
////            }
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }
////    }
//
//    @Before("controllerMethods()")
//    public void beforeControllerMethod() {
//    	log.info("Logged in user in pointcut username:{}",loginService.getLoggedInUser());
//        try {
//        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        	String latitudeHeader = request.getHeader("Latitude");
//            String longitudeHeader = request.getHeader("Longitude");
//            String username = loginService.getLoggedInUser();
//                if (latitudeHeader != null && longitudeHeader != null) {
//                	double latitude = Double.parseDouble(latitudeHeader);
//                    double longitude = Double.parseDouble(longitudeHeader);
//                    UserLocation userLocation = new UserLocation();
//                    userLocation.setLatitude(latitude);
//                    userLocation.setLongitude(longitude);
//                    userLocation.setUsername(loginService.getLoggedInUser());
//                    log.info("userLocation:"+userLocation);
//                    userLocationRepository.save(userLocation);
//                    log.info("Username: "+username+", Latitude: " + latitude + ", Longitude: " + longitude + " saved to database.");
//                } else {
//                    log.info("Lat & Long is null.");
//                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    private Field findField(Class<?> clazz, String fieldName) {
//        while (clazz != null) {
//            try {
//                return clazz.getDeclaredField(fieldName);
//            } catch (NoSuchFieldException e) {
//                clazz = clazz.getSuperclass();
//            }
//        }
//        return null;
//    }
//}
//package com;


