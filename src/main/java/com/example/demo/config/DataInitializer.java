package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Check if admin already exists
            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("✅ Default ADMIN created");
            }
        };
    }
}
