package com.hpy.oauth.util;

public class ResponseMessageUtil {
    public static String getMessageForStatusCode(int statusCode) {
        switch (statusCode) {
            case 200:
                return "Successfully logged in";
            case 400:
                return "UnAuthorized: Invalid Credentials";
            case 401:
                return "Session Timed Out! Please Login Again"; 
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown Error";
        }
    }
}
