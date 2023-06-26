package com.registration.course.serverapp.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.course.serverapp.api.dto.request.EmailRequest;
import com.registration.course.serverapp.api.services.EmailService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailController {
    private EmailService emailService;

  @PostMapping("/mail")
  public EmailRequest sendEmailTemplat(@RequestBody EmailRequest emailRequest) {
    return emailService.sendEmailTemplat(emailRequest);
  }
}