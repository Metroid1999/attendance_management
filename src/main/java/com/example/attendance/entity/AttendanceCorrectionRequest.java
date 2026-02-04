package com.example.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "attendance_correction_requests")
public class AttendanceCorrectionRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "work_date", nullable = false)
  private LocalDate workDate;

  @Column(name = "requested_start_time", nullable = false)
  private LocalDateTime requestedStartTime;

  @Column(name = "requested_end_time", nullable = false)
  private LocalDateTime requestedEndTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatus status;

  protected AttendanceCorrectionRequest() {
  }

  public AttendanceCorrectionRequest(User user, LocalDate workDate, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime) {
    this.user = Objects.requireNonNull(user, "user");
    this.workDate = Objects.requireNonNull(workDate, "workDate");
    this.requestedStartTime = Objects.requireNonNull(requestedStartTime, "requestedStartTime");
    this.requestedEndTime = Objects.requireNonNull(requestedEndTime, "requestedEndTime");
    this.status = RequestStatus.PENDING;
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

  public LocalDateTime getRequestedStartTime() {
    return requestedStartTime;
  }

  public LocalDateTime getRequestedEndTime() {
    return requestedEndTime;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = Objects.requireNonNull(status, "status");
  }
}
