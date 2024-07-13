package com.registration.course.serverapp.api.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsernameOrMember_Email(String username, String email);

  // Query Method
  public boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
