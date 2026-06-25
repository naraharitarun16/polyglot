package com.polyglot.chat.security;

import java.util.UUID;

public class JwtUtil {

    public static String generateToken(String username) {
        return "TOKEN_" + username + "_" + UUID.randomUUID();
    }
}
