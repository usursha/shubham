package com.hpy.ops360.dashboard.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;


public final class SafeValueUtil {
	
	private SafeValueUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
	
    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static Integer nullToZero(Integer value) {
        return value == null ? 0 : value;
    }

    public static Long nullToZero(Long value) {
        return value == null ? 0L : value;
    }

    public static Double nullToZero(Double value) {
        return value == null ? 0.0 : value;
    }

    public static boolean nullToFalse(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static <T> List<T> nullToEmptyList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Set<T> nullToEmptySet(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <K, V> Map<K, V> nullToEmptyMap(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    public static String trimOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    public static String safeToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
