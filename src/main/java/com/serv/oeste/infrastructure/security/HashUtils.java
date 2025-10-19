package com.serv.oeste.infrastructure.security;

import com.serv.oeste.domain.exceptions.auth.AuthRefreshTokenRevokedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HashUtils {
    public static String sha256Hex(String rawString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(messageDigest.digest(rawString.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            throw new AuthRefreshTokenRevokedException();
        }
    }
}
