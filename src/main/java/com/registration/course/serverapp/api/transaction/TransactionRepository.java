package com.registration.course.serverapp.api.transaction;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
  List<Transaction> findAllByMemberId(Integer memberId, Sort sort);

  List<Transaction> findAllByMemberIdAndIsRegistered(Integer memberId, boolean isRegistered);
}
