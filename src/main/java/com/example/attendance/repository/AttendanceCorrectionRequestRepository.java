package com.example.attendance.repository;

import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.entity.RequestStatus;
import com.example.attendance.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceCorrectionRequestRepository extends JpaRepository<AttendanceCorrectionRequest, Long> {
  List<AttendanceCorrectionRequest> findByUserOrderByWorkDateDesc(User user);

  List<AttendanceCorrectionRequest> findByStatusOrderByWorkDateDesc(RequestStatus status);

  List<AttendanceCorrectionRequest> findAllByOrderByWorkDateDesc();
}
