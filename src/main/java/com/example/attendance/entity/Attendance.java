package com.example.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "attendances", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "work_date"})
})
public class Attendance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "work_date", nullable = false)
  private LocalDate workDate;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "working_minutes", nullable = false)
  private int workingMinutes;

  @Column(name = "break_minutes", nullable = false)
  private int breakMinutes;

  protected Attendance() {
  }

  public Attendance(User user, LocalDate workDate, LocalDateTime startTime) {
    this.user = Objects.requireNonNull(user, "user");
    this.workDate = Objects.requireNonNull(workDate, "workDate");
    this.startTime = Objects.requireNonNull(startTime, "startTime");
    this.workingMinutes = 0;
    this.breakMinutes = 0;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public LocalDate getWorkDate() {
    return workDate;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = Objects.requireNonNull(startTime, "startTime");
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = Objects.requireNonNull(endTime, "endTime");
  }

  public int getWorkingMinutes() {
    return workingMinutes;
  }

  public void setWorkingMinutes(int workingMinutes) {
    this.workingMinutes = workingMinutes;
  }

  public int getBreakMinutes() {
    return breakMinutes;
  }

  public void setBreakMinutes(int breakMinutes) {
    this.breakMinutes = breakMinutes;
  }
}
