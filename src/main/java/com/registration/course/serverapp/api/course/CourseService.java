package com.registration.course.serverapp.api.course;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.dto.response.CoursesMember;
import com.registration.course.serverapp.api.transaction.Transaction;
import com.registration.course.serverapp.api.transaction.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  private final TransactionRepository transactionRepository;

  public List<Course> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    return courseRepository.findAll(sort);
  }

  public Course create(Course course) {

    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
    course.setCreatedAt(timestamp);

    return courseRepository.save(course);
  }

  public Course getById(Integer id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new EmptyResultDataAccessException("Data course tersebut", 0));
  }

  public Course update(Integer id, Course course) {
    this.getById(id);
    course.setId(id);
    return courseRepository.save(course);
  }

  public Course delete(Integer id) {
    Course course = this.getById(id);
    courseRepository.delete(course);
    return course;
  }

  public List<CoursesMember> getAllCourseByMemberIdSession(Integer memberId) {
   List<Course> courses = getAll();
   List<Transaction> transactions = transactionRepository.findAllByMemberIdAndIsRegistered(memberId, true);

  // Map courses to CoursesMember and set isRegistered field
  List<CoursesMember> coursesMemberList = courses.stream()
  .map(course -> {
      // Check if the course is registered
      boolean isRegistered = transactions.stream()
          .anyMatch(transaction -> transaction.getCourse().getId().equals(course.getId()) && transaction.getIsRegistered());

      // Create CoursesMember object and set isRegistered
      CoursesMember coursesMember = new CoursesMember();
      coursesMember.setCourse(course);
      coursesMember.setIsRegistered(isRegistered);

      return coursesMember;
  })
  .collect(Collectors.toList());

return coursesMemberList;
  }

}
