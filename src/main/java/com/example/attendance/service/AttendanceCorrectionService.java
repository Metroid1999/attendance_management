package com.example.attendance.service;

import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.entity.RequestStatus;
import com.example.attendance.entity.User;
import com.example.attendance.exception.BusinessException;
import com.example.attendance.repository.AttendanceCorrectionRequestRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceCorrectionService {
  private final AttendanceCorrectionRequestRepository correctionRepository;
  private final AttendanceService attendanceService;

  public AttendanceCorrectionService(AttendanceCorrectionRequestRepository correctionRepository,
                                     AttendanceService attendanceService) {
    this.correctionRepository = correctionRepository;
    this.attendanceService = attendanceService;
  }

  public List<AttendanceCorrectionRequest> getRequestsForUser(User user) {
    return correctionRepository.findByUserOrderByWorkDateDesc(user);
  }

  public List<AttendanceCorrectionRequest> getAllRequests() {
    return correctionRepository.findAllByOrderByWorkDateDesc();
  }

  @Transactional
  public AttendanceCorrectionRequest submitRequest(User user, LocalDate workDate,
                                                   LocalDateTime requestedStart,
                                                   LocalDateTime requestedEnd) {
    if (requestedStart.isAfter(requestedEnd)) {
      throw new BusinessException("Start time must be before end time.");
    }
    AttendanceCorrectionRequest request = new AttendanceCorrectionRequest(user, workDate, requestedStart, requestedEnd);
    return correctionRepository.save(request);
  }

  @Transactional
  public AttendanceCorrectionRequest approve(Long requestId) {
    AttendanceCorrectionRequest request = correctionRepository.findById(requestId)
        .orElseThrow(() -> new BusinessException("Request not found."));
    if (request.getStatus() != RequestStatus.PENDING) {
      throw new BusinessException("Request is already processed.");
    }
    attendanceService.updateAttendance(
        request.getUser(),
        request.getWorkDate(),
        request.getRequestedStartTime(),
        request.getRequestedEndTime()
    );
    request.setStatus(RequestStatus.APPROVED);
    return request;
  }

  @Transactional
  public AttendanceCorrectionRequest reject(Long requestId) {
    AttendanceCorrectionRequest request = correctionRepository.findById(requestId)
        .orElseThrow(() -> new BusinessException("Request not found."));
    if (request.getStatus() != RequestStatus.PENDING) {
      throw new BusinessException("Request is already processed.");
    }
    request.setStatus(RequestStatus.REJECTED);
    return request;
  }
}
