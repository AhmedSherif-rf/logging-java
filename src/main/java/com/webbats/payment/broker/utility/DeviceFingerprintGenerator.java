package com.webbats.payment.broker.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DeviceFingerprintGenerator {

    private DeviceFingerprintGenerator() throws InstantiationException {
        throw new InstantiationException("Util class");
    }

    public static String generateDeviceFingerprint(String userAgent, String ipAddress) throws NoSuchAlgorithmException {
        String input = userAgent + ipAddress;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String generateDeviceFingerprintFromHttpRequest(HttpServletRequest request) {
        try {
            String userAgent = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();

            return generateDeviceFingerprint(userAgent, ipAddress);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateDeviceFingerprintStompHeaderAccessor(StompHeaderAccessor stompHeaderAccessor) {
        try {
            String userAgent = stompHeaderAccessor.getSessionAttributes().get("user-agent").toString();
            String ipAddress = stompHeaderAccessor.getSessionAttributes().get("ip").toString().replace("/", "");
            return generateDeviceFingerprint(userAgent, ipAddress);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
