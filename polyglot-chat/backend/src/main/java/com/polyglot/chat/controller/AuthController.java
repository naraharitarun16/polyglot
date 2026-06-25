package com.polyglot.chat.controller;

import com.polyglot.chat.model.User;
import com.polyglot.chat.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Simple authentication: hardcoded users for demo
        if ("user1".equals(user.getUsername()) && "password1".equals(user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
        } else if ("user2".equals(user.getUsername()) && "password2".equals(user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}");
        }
    }
}
