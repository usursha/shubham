package com.hpy.oauth.advice;

public class CustomExceptions {

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
}
