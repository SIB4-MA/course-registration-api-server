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
  UserRespository userRespository;

  public List<User> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    return userRespository.findAll(sort);
  }

  public User getUserbyId(Integer id) {
    return userRespository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("User", 0));
  }

  public User getByUsername(String username) {
    return userRespository.findByUsername(username).orElseThrow(() -> new EmptyResultDataAccessException("User", 0));
  }

}
