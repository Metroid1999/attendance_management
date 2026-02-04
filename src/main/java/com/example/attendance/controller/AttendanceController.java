package com.example.attendance.controller;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AttendanceController {
  private final AttendanceService attendanceService;
  private final UserService userService;

  public AttendanceController(AttendanceService attendanceService, UserService userService) {
    this.attendanceService = attendanceService;
    this.userService = userService;
  }

  @GetMapping("/attendances")
  public String today(Model model, Authentication authentication) {
    User user = userService.getByEmail(authentication.getName());
    LocalDate today = LocalDate.now();
    Optional<Attendance> attendance = attendanceService.getAttendance(user, today);
    model.addAttribute("today", today);
    model.addAttribute("attendance", attendance.orElse(null));
    return "attendances";
  }

  @PostMapping("/attendances/start")
  public String clockIn(Authentication authentication) {
    User user = userService.getByEmail(authentication.getName());
    attendanceService.clockIn(user, LocalDateTime.now());
    return "redirect:/attendances";
  }

  @PostMapping("/attendances/end")
  public String clockOut(Authentication authentication) {
    User user = userService.getByEmail(authentication.getName());
    attendanceService.clockOut(user, LocalDateTime.now());
    return "redirect:/attendances";
  }

  @GetMapping("/attendances/daily")
  public String daily(@RequestParam(name = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      Model model,
                      Authentication authentication) {
    User user = userService.getByEmail(authentication.getName());
    LocalDate target = date != null ? date : LocalDate.now();
    Optional<Attendance> attendance = attendanceService.getAttendance(user, target);
    model.addAttribute("date", target);
    model.addAttribute("attendance", attendance.orElse(null));
    return "daily";
  }

  @GetMapping("/attendances/month")
  public String monthly(@RequestParam(name = "year", required = false) Integer year,
                        @RequestParam(name = "month", required = false) Integer month,
                        Model model,
                        Authentication authentication) {
    User user = userService.getByEmail(authentication.getName());
    YearMonth target = (year != null && month != null)
        ? YearMonth.of(year, month)
        : YearMonth.now();
    List<Attendance> attendances = attendanceService.getMonthlyAttendances(user, target);
    model.addAttribute("targetMonth", target);
    model.addAttribute("attendances", attendances);
    return "attendance_list";
  }
}
