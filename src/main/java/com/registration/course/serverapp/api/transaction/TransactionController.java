package com.registration.course.serverapp.api.transaction;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.course.serverapp.api.authentication.AppUserDetail;
import com.registration.course.serverapp.api.dto.request.TransactionRequest;
import com.registration.course.serverapp.api.dto.response.ResponseData;
import com.registration.course.serverapp.api.user.User;
import com.registration.course.serverapp.api.user.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private UserService userService;

  // admin
  @GetMapping
  public ResponseEntity<ResponseData<Transaction>> getAllTransactions() {
    ResponseData<Transaction> responseData = new ResponseData<>();
    responseData.setStatus(true);
    responseData.getMessages().add("success");
    responseData.setPlayload(transactionService.getAll());
    return ResponseEntity.ok(responseData);
  }

  // User menambahkan course
  @PostMapping
  public ResponseEntity<ResponseData<Transaction>> addTransaction(@RequestBody TransactionRequest transactionRequest) {
    ResponseData<Transaction> responseData = new ResponseData<>();

    // if (errors.hasErrors()) {
    // for (ObjectError error : errors.getAllErrors()) {
    // responseData.getMessages().add(error.getDefaultMessage());
    // }
    // responseData.setStatus(false);
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    // }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object principal = authentication.getPrincipal();
      Object credentials = authentication.getCredentials();
      boolean isAuthenticated = authentication.isAuthenticated();
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      // Lakukan pemrosesan lebih lanjut sesuai kebutuhan

      // Contoh mengambil informasi spesifik dari principal
      if (principal instanceof AppUserDetail) {
        AppUserDetail appUserDetail = (AppUserDetail) principal;
        String username = appUserDetail.getUsername();
        // Melakukan pemrosesan lebih lanjut dengan informasi pengguna
        User user = userService.getByUsername(username);
        transactionRequest.setMemberId(user.getId());
      }
    }

    responseData.setStatus(true);
    responseData.getMessages().add("course berhasil ditambahkan ke transaction");
    responseData.getPlayload().add(transactionService.create(transactionRequest));
    return ResponseEntity.ok(responseData);
  }

  // Admin dapat mengubah status
  @PutMapping("/{id}")
  public ResponseEntity<ResponseData<Transaction>> updateMember(@Valid @RequestBody Transaction transaction,
      @PathVariable Integer id,
      Errors errors) {
    ResponseData<Transaction> responseData = new ResponseData<>();
    if (errors.hasErrors()) {
      for (ObjectError error : errors.getAllErrors()) {
        responseData.getMessages().add(error.getDefaultMessage());
      }
      responseData.setStatus(false);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }
    responseData.getPlayload().add(transactionService.update(id, transaction));
    responseData.setStatus(true);
    responseData.getMessages().add("transaction berhasil diperbarui");
    return ResponseEntity.ok(responseData);
  }

}
