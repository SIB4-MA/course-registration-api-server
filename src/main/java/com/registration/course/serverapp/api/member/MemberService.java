package com.registration.course.serverapp.api.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

  @Autowired
  MemberRepository memberRepository;

  public List<Member> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "user.createdAt");
    return memberRepository.findAll(sort);
  }

  public Member getById(Integer id) {
    return memberRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("member ", 0));
  }

  public Member update(Integer id, Member member) {
    Member checkingMember = this.getById(id);
    if (!checkingMember.getEmail().equalsIgnoreCase(member.getEmail())) {
      if (memberRepository.existsByEmail(member.getEmail())) {
        throw new DataIntegrityViolationException("Email");
      }
    }
    member.setId(id);
    return memberRepository.save(member);
  }

  public Member updateCourseActiveById(Integer Id) {
    Member member = this.getById(Id);
    member.setId(Id);
    Integer currentActiveCourse = member.getActiveCourse();
    member.setActiveCourse(currentActiveCourse + 1);
    return memberRepository.save(member);
  }

}
