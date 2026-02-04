package com.example.attendance.repository;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
  Optional<Attendance> findByUserAndWorkDate(User user, LocalDate workDate);

  List<Attendance> findByUserAndWorkDateBetweenOrderByWorkDateAsc(User user, LocalDate start, LocalDate end);
}
