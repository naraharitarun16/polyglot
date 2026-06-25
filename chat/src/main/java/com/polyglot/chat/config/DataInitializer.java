package com.polyglot.chat.config;

import com.polyglot.chat.model.User;
import com.polyglot.chat.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create default admin user if not exists
            if (!userRepository.existsByUsername("sa")) {
                User adminUser = new User(
                    "sa",
                    passwordEncoder.encode("chat123"),
                    "admin@chat.com",
                    "System Admin"
                );
                adminUser.setCreatedAt(LocalDateTime.now());
                adminUser.setActive(true);
                userRepository.save(adminUser);
                System.out.println("Default admin user created: sa / chat123");
            }
        };
    }
}

