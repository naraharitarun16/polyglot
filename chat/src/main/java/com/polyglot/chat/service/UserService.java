package com.polyglot.chat.service;

import com.polyglot.chat.model.User;
import com.polyglot.chat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Email validation regex
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(String username, String password, String email, String fullName) {
        // Validate input
        validateRegistrationInput(username, password, email);

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email '" + email + "' is already registered");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save new user
        User user = new User(username, encodedPassword, email, fullName);
        return userRepository.save(user);
    }

    public User registerUser(String username, String password, String email) {
        return registerUser(username, password, email, null);
    }

    public User createUser(String username, String password) {
        User user = new User(username, passwordEncoder.encode(password), null);
        return userRepository.save(user);
    }

    public void updateLastLogin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setLastLogin(LocalDateTime.now());
            userRepository.save(existingUser);
        }
    }

    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            // Check if user is active
            if (!foundUser.isActive()) {
                throw new IllegalArgumentException("User account is deactivated");
            }
            // Verify password with bcrypt
            return passwordEncoder.matches(password, foundUser.getPassword());
        }
        return false;
    }

    private void validateRegistrationInput(String username, String password, String email) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 2) {
            throw new IllegalArgumentException("Username must be at least 2 characters");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username cannot exceed 50 characters");
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public long getTotalUsersCount() {
        return userRepository.count();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}

