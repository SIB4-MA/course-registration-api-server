package com.registration.course.serverapp.api.authentication;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.course.serverapp.api.dto.request.LoginRequest;
import com.registration.course.serverapp.api.dto.request.UserRequest;
import com.registration.course.serverapp.api.dto.response.LoginResponse;
import com.registration.course.serverapp.api.dto.response.ResponseData;
import com.registration.course.serverapp.api.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/api/register")
  public ResponseEntity<ResponseData<User>> register(@Valid @RequestBody UserRequest userRequest, Errors errors) {
    ResponseData<User> responseData = new ResponseData<>();

    if (errors.hasErrors()) {
      for (ObjectError error : errors.getAllErrors()) {
        responseData.getMessages().add(error.getDefaultMessage());
      }
      responseData.setStatus(false);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    responseData.setStatus(true);
    responseData.getMessages().add("Akun berhasil didaftarkan");
    responseData.getPayload().add(authService.register(userRequest));
    return ResponseEntity.ok(responseData);
  }

  @PostMapping("/api/login")
  public ResponseEntity<ResponseData<LoginResponse>> register(@Valid @RequestBody LoginRequest loginRequest,
      Errors errors) {
    ResponseData<LoginResponse> responseData = new ResponseData<>();

    if (errors.hasErrors()) {
      for (ObjectError error : errors.getAllErrors()) {
        responseData.getMessages().add(error.getDefaultMessage());
      }
      responseData.setStatus(false);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    responseData.setStatus(true);
    responseData.getMessages().add("berhasil login");
    responseData.getPayload().add(authService.login(loginRequest));
    return ResponseEntity.ok(responseData);
  }
}
