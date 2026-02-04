package com.example.attendance.config;

import com.example.attendance.entity.Role;
import com.example.attendance.entity.User;
import com.example.attendance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
  @Bean
  public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      userRepository.findByEmail("user@example.com")
          .orElseGet(() -> userRepository.save(new User(
              "User",
              "user@example.com",
              passwordEncoder.encode("password"),
              Role.ROLE_USER
          )));

      userRepository.findByEmail("admin@example.com")
          .orElseGet(() -> userRepository.save(new User(
              "Admin",
              "admin@example.com",
              passwordEncoder.encode("adminpass"),
              Role.ROLE_ADMIN
          )));
    };
  }
}
