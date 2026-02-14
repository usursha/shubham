package com.hpy.oauth.enums;

public enum TokenStatus {
    ACTIVE,        // Token is active and can be used
    BLACKLISTED,   // Token is blacklisted and cannot be used
    EXPIRED        // Token has expired and is no longer valid
}
