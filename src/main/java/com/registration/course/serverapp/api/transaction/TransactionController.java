package com.registration.course.serverapp.api.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.course.serverapp.api.authentication.AppUserDetail;
import com.registration.course.serverapp.api.dto.request.TransactionRequest;
import com.registration.course.serverapp.api.dto.request.TransactionStatusAndIsRegisteredRequest;
import com.registration.course.serverapp.api.dto.response.ResponseData;
import com.registration.course.serverapp.api.user.User;
import com.registration.course.serverapp.api.user.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor
@NoArgsConstructor
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
    responseData.setPayload(transactionService.getAll());
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseData<Transaction>> getTransactionById(@PathVariable Integer id) {
    ResponseData<Transaction> responseData = new ResponseData<>();
    responseData.setStatus(true);
    responseData.getMessages().add("berhasil ditemukan");
    responseData.getPayload().add(transactionService.getById(id));
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
      // Object credentials = authentication.getCredentials();
      // boolean isAuthenticated = authentication.isAuthenticated();
      // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
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
    responseData.getPayload().add(transactionService.create(transactionRequest));
    return ResponseEntity.ok(responseData);
  }

  // Admin dapat mengubah status dan is_registered
  @PutMapping("/{id}")
  public ResponseEntity<ResponseData<Transaction>> updateMember(
      @RequestBody TransactionStatusAndIsRegisteredRequest transactionStatusAndIsRegisteredRequest,
      @PathVariable Integer id) {
    ResponseData<Transaction> responseData = new ResponseData<>();

    responseData.getPayload().add(transactionService.update(id, transactionStatusAndIsRegisteredRequest));
    responseData.setStatus(true);
    responseData.getMessages().add("transaction berhasil diperbarui");
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/member-transactions")
  public ResponseEntity<ResponseData<Transaction>> getAllTransactionByMemberIdSessioj() {
    ResponseData<Transaction> responseData = new ResponseData<>();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof AppUserDetail) {
        AppUserDetail appUserDetail = (AppUserDetail) principal;
        String username = appUserDetail.getUsername();
        User user = userService.getByUsername(username);
        responseData.setPayload(transactionService.getAllTransactionsByMemberId(user.getId()));
      }
    }

    responseData.setStatus(true);
    responseData.getMessages().add("berhasil ditemukan");
    return ResponseEntity.ok(responseData);
  }

  // ambil all course yang sudah terdaftar
  @GetMapping("/course-registered")
  public ResponseEntity<ResponseData<Transaction>> getAllTransactionByMemberIdSessionAndisRegistered() {
    ResponseData<Transaction> responseData = new ResponseData<>();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof AppUserDetail) {
        AppUserDetail appUserDetail = (AppUserDetail) principal;
        String username = appUserDetail.getUsername();
        // Melakukan pemrosesan lebih lanjut dengan informasi pengguna
        User user = userService.getByUsername(username);
        responseData.setPayload(transactionService.getAllTransactionsByMemberIdSessionAndIsRegistered(user.getId()));
      }
    }
    responseData.setStatus(true);
    responseData.getMessages().add("data berhasil di dapatkan");
    return ResponseEntity.ok(responseData);
  }

}
