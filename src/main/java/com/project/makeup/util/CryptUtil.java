package com.project.makeup.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptUtil {
     public static String encode(String password) {
        byte[] encodedBytes = Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        return encodedString;
    }

    public static String decode(String password) {
        byte[] decodedBytes = Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8));
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedString;
    }
}
