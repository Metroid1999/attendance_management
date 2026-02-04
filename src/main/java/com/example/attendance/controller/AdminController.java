package com.example.attendance.controller;

import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.service.AttendanceCorrectionService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
  private final AttendanceCorrectionService correctionService;

  public AdminController(AttendanceCorrectionService correctionService) {
    this.correctionService = correctionService;
  }

  @GetMapping("/admin/corrections")
  public String list(Model model) {
    List<AttendanceCorrectionRequest> requests = correctionService.getAllRequests();
    model.addAttribute("requests", requests);
    return "correction_list";
  }

  @PostMapping("/admin/corrections/approve")
  public String approve(@RequestParam("requestId") Long requestId) {
    correctionService.approve(requestId);
    return "redirect:/admin/corrections";
  }

  @PostMapping("/admin/corrections/reject")
  public String reject(@RequestParam("requestId") Long requestId) {
    correctionService.reject(requestId);
    return "redirect:/admin/corrections";
  }
}
