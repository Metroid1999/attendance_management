package com.example.attendance.service;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;
import com.example.attendance.exception.BusinessException;
import com.example.attendance.repository.AttendanceRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {
  private final AttendanceRepository attendanceRepository;

  public AttendanceService(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }

  public Optional<Attendance> getAttendance(User user, LocalDate workDate) {
    return attendanceRepository.findByUserAndWorkDate(user, workDate);
  }

  public List<Attendance> getMonthlyAttendances(User user, YearMonth month) {
    LocalDate start = month.atDay(1);
    LocalDate end = month.atEndOfMonth();
    return attendanceRepository.findByUserAndWorkDateBetweenOrderByWorkDateAsc(user, start, end);
  }

  @Transactional
  public Attendance clockIn(User user, LocalDateTime now) {
    LocalDate today = now.toLocalDate();
    attendanceRepository.findByUserAndWorkDate(user, today)
        .ifPresent(a -> {
          throw new BusinessException("Already clocked in for today.");
        });
    Attendance attendance = new Attendance(user, today, now);
    return attendanceRepository.save(attendance);
  }

  @Transactional
  public Attendance clockOut(User user, LocalDateTime now) {
    LocalDate today = now.toLocalDate();
    Attendance attendance = attendanceRepository.findByUserAndWorkDate(user, today)
        .orElseThrow(() -> new BusinessException("Please clock in first."));
    if (attendance.getEndTime() != null) {
      throw new BusinessException("Already clocked out for today.");
    }
    LocalDateTime start = attendance.getStartTime();
    if (start == null) {
      throw new BusinessException("Start time is missing.");
    }
    attendance.setEndTime(now);
    int minutes = (int) Duration.between(start, now).toMinutes();
    if (minutes < 0) {
      throw new BusinessException("Invalid time range.");
    }
    int breakMinutes = calculateBreakMinutes(minutes);
    attendance.setBreakMinutes(breakMinutes);
    attendance.setWorkingMinutes(Math.max(minutes - breakMinutes, 0));
    return attendance;
  }

  @Transactional
  public Attendance updateAttendance(User user, LocalDate workDate, LocalDateTime start, LocalDateTime end) {
    Attendance attendance = attendanceRepository.findByUserAndWorkDate(user, workDate)
        .orElseThrow(() -> new BusinessException("Attendance not found for the date."));
    if (start.isAfter(end)) {
      throw new BusinessException("Start time must be before end time.");
    }
    attendance.setStartTime(start);
    attendance.setEndTime(end);
    int minutes = (int) Duration.between(start, end).toMinutes();
    int breakMinutes = calculateBreakMinutes(minutes);
    attendance.setBreakMinutes(breakMinutes);
    attendance.setWorkingMinutes(Math.max(minutes - breakMinutes, 0));
    return attendance;
  }

  private int calculateBreakMinutes(int totalMinutes) {
    return totalMinutes >= 360 ? 60 : 0;
  }
}
