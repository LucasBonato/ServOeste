package com.serv.oeste.domain.utils;

public class StringUtils {
    private StringUtils() {}

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String onlyDigits(String str) {
        return str == null ? null : str.replaceAll("\\D", "");
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
