package com.webbats.payment.broker.common.constants;

public class AppConstants {
    public static final String API_VERSION_1 = "api/v1";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String API_BROKER = "api/broker";
    public static final String API_CLIENT = "api/client";
    public static final String API_COMMON = "api/common";
    public static final String API_ROLE = API_BROKER + "/roles";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\S](?=.*?([^\\w\\s]|[_])).{7,}$";
    public static final String PHONE_REGEX = "^\\+\\d+$";

    private AppConstants() {
        throw new AssertionError();
    }
}