package com.example.attendance.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  protected User() {
  }

  public User(String name, String email, String password, Role role) {
    this.name = Objects.requireNonNull(name, "name");
    this.email = Objects.requireNonNull(email, "email");
    this.password = Objects.requireNonNull(password, "password");
    this.role = Objects.requireNonNull(role, "role");
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = Objects.requireNonNull(email, "email");
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = Objects.requireNonNull(password, "password");
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = Objects.requireNonNull(role, "role");
  }
}
