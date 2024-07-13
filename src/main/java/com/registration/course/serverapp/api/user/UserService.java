package com.registration.course.serverapp.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

  @Autowired
  UserRepository userRepository;

  public List<User> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    return userRepository.findAll(sort);
  }

  public User getUserbyId(Integer id) {
    return userRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("User", 0));
  }

  public User getByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new EmptyResultDataAccessException("User", 0));
  }

}
