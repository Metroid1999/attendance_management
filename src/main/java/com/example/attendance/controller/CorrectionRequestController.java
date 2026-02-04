package com.example.attendance.controller;

import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.entity.User;
import com.example.attendance.service.AttendanceCorrectionService;
import com.example.attendance.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CorrectionRequestController {
  private final AttendanceCorrectionService correctionService;
  private final UserService userService;

  public CorrectionRequestController(AttendanceCorrectionService correctionService, UserService userService) {
    this.correctionService = correctionService;
    this.userService = userService;
  }

  @GetMapping("/corrections")
  public String list(Authentication authentication, Model model) {
    User user = userService.getByEmail(authentication.getName());
    List<AttendanceCorrectionRequest> requests = correctionService.getRequestsForUser(user);
    model.addAttribute("requests", requests);
    return "correction_request";
  }

  @PostMapping("/corrections")
  public String submit(Authentication authentication,
                       @RequestParam("workDate")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate,
                       @RequestParam("requestedStartTime")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestedStartTime,
                       @RequestParam("requestedEndTime")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestedEndTime) {
    User user = userService.getByEmail(authentication.getName());
    correctionService.submitRequest(user, workDate, requestedStartTime, requestedEndTime);
    return "redirect:/corrections";
  }
}
