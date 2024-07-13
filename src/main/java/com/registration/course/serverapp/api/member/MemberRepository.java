package com.registration.course.serverapp.api.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
  public boolean existsByEmail(String email);

  public boolean existsByName(String name);
}
