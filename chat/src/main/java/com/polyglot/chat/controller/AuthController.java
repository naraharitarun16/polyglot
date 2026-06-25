package com.polyglot.chat.controller;

import com.polyglot.chat.dto.AuthResponse;
import com.polyglot.chat.dto.SignupRequest;
import com.polyglot.chat.model.User;
import com.polyglot.chat.security.JwtUtil;
import com.polyglot.chat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Validate user credentials against database
            if (userService.validateUser(user.getUsername(), user.getPassword())) {
                userService.updateLastLogin(user.getUsername());
                String token = JwtUtil.generateToken(user.getUsername());
                
                User loggedInUser = userService.findByUsername(user.getUsername()).get();
                AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    loggedInUser.getId(),
                    loggedInUser.getUsername(),
                    loggedInUser.getEmail(),
                    loggedInUser.getFullName()
                );
                
                AuthResponse response = new AuthResponse(
                    true,
                    "Login successful",
                    token,
                    userInfo
                );
                return ResponseEntity.ok(response);
            } else {
                AuthResponse response = new AuthResponse(false, "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            // Validate input
            if (signupRequest.getUsername() == null || signupRequest.getUsername().trim().isEmpty()) {
                AuthResponse response = new AuthResponse(false, "Username is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (signupRequest.getPassword() == null || signupRequest.getPassword().trim().isEmpty()) {
                AuthResponse response = new AuthResponse(false, "Password is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
                AuthResponse response = new AuthResponse(false, "Email is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Register the user
            User newUser = userService.registerUser(
                signupRequest.getUsername(),
                signupRequest.getPassword(),
                signupRequest.getEmail(),
                signupRequest.getFullName()
            );

            // Generate token
            String token = JwtUtil.generateToken(newUser.getUsername());
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getEmail(),
                newUser.getFullName()
            );

            AuthResponse response = new AuthResponse(
                true,
                "User registered successfully! Please log in.",
                token,
                userInfo
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            AuthResponse response = new AuthResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) {
        // Alias for signup endpoint
        return signup(signupRequest);
    }

    @GetMapping("/users/count")
    public ResponseEntity<?> getUsersCount() {
        try {
            long count = userService.getTotalUsersCount();
            return ResponseEntity.ok().body("{\"totalUsers\":" + count + "}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}


