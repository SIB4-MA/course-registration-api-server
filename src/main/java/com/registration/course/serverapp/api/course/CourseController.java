package com.registration.course.serverapp.api.course;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.course.serverapp.api.authentication.AppUserDetail;
import com.registration.course.serverapp.api.dto.response.CoursesMember;
import com.registration.course.serverapp.api.dto.response.ResponseData;
import com.registration.course.serverapp.api.user.User;
import com.registration.course.serverapp.api.user.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

  @Autowired
  private CourseService courseService;

  private final UserService userService;

  @GetMapping
  public ResponseEntity<ResponseData<Course>> getAllCourses() {
    ResponseData<Course> responseData = new ResponseData<>();
    responseData.setStatus(true);
    responseData.getMessages().add("success");
    responseData.setPayload(courseService.getAll());
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/member")
  public ResponseEntity<ResponseData<CoursesMember>> getAllCoursesMember() {
    ResponseData<CoursesMember> responseData = new ResponseData<>();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof AppUserDetail) {
        AppUserDetail appUserDetail = (AppUserDetail) principal;
        String username = appUserDetail.getUsername();
        User user = userService.getByUsername(username);
        responseData.setPayload(courseService.getAllCourseByMemberIdSession(user.getId()));
      }
    }

    responseData.setStatus(true);
    responseData.getMessages().add("success");
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseData<Course>> getUserById(@PathVariable Integer id) {
    ResponseData<Course> responseData = new ResponseData<>();
    responseData.setStatus(true);
    responseData.getMessages().add("berhasil ditemukan");
    responseData.getPayload().add(courseService.getById(id));
    return ResponseEntity.ok(responseData);
  }

  @PostMapping
  public ResponseEntity<ResponseData<Course>> create(@Validated @RequestBody Course course, Errors errors) {
    ResponseData<Course> responseData = new ResponseData<>();
    if (errors.hasErrors()) {
      for (ObjectError error : errors.getAllErrors()) {
        responseData.getMessages().add(error.getDefaultMessage());
      }
      responseData.setStatus(false);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }
    responseData.setStatus(true);
    responseData.getPayload().add(courseService.create(course));
    responseData.getMessages().add("Course " + course.getTitle() + " berhasil ditambahkan");
    return ResponseEntity.ok(responseData);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseData<Course>> update(@Valid @RequestBody Course course, @PathVariable Integer id,
      Errors errors) {
    ResponseData<Course> responseData = new ResponseData<>();
    if (errors.hasErrors()) {
      for (ObjectError error : errors.getAllErrors()) {
        responseData.getMessages().add(error.getDefaultMessage());
      }
      responseData.setStatus(false);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }
    responseData.setStatus(true);
    responseData.getMessages().add("data course berhasil diedit");
    responseData.getPayload().add(courseService.update(id, course));
    return ResponseEntity.ok(responseData);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseData<Course>> delete(@PathVariable Integer id) {
    ResponseData<Course> responseData = new ResponseData<>();

    responseData.setStatus(true);
    responseData.getMessages().add("data berhasil dihapus");
    responseData.getPayload().add(courseService.delete(id));
    return ResponseEntity.ok(responseData);
  }

}
