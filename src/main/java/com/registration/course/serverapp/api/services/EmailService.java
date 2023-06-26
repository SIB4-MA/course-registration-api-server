package com.registration.course.serverapp.api.services;

import java.io.File;
import java.util.Scanner;

import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.dto.request.EmailRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
  
  public EmailRequest sendEmailTemplat(EmailRequest emailRequest) {
    try {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
  
    helper.setTo(emailRequest.getTo());
    helper.setSubject(emailRequest.getSubject());
  
    FileSystemResource htmlFile = new FileSystemResource(new
    File(emailRequest.getText()));
    Scanner scanner = new Scanner(htmlFile.getFile());
    scanner.useDelimiter("\\Z");
    String htmlBody = scanner.next();
    scanner.close();
  
    helper.setText(htmlBody, true);
  
    mailSender.send(message);
    } catch (Exception e) {
    throw new IllegalStateException("Email false to send!!!");
    }
    return emailRequest;
    }
}
