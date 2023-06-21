package com.registration.course.serverapp.api.transaction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.course.Course;
import com.registration.course.serverapp.api.course.CourseService;
import com.registration.course.serverapp.api.dto.request.TransactionRequest;
import com.registration.course.serverapp.api.member.Member;
import com.registration.course.serverapp.api.member.MemberService;
import com.registration.course.serverapp.api.transaction.history.HistoryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CourseService courseService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private HistoryService historyService;

  public List<Transaction> getAll() {
    return transactionRepository.findAll();

  }

  public Transaction create(TransactionRequest transactionRequest) {
    // Transaction transaction = modelMapper.map(transactionRequest,
    // Transaction.class);
    // transaction.setCourse(courseService.getById(transactionRequest.getCourseId()));
    // transaction.setMember(memberService.getById(transactionRequest.getMemberId()));

    Transaction transaction = new Transaction();
    Course course = courseService.getById(transactionRequest.getCourseId());
    Member member = memberService.getById(transactionRequest.getMemberId());

    transaction.setCourse(course);
    transaction.setMember(member);

    // set created_at -> timestamp
    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
    transaction.setCreated_at(timestamp);

    // set status default -> enum SUCCESS, PROCESS, FAIL
    transaction.setStatus(TransactionStatus.PROCESS);

    // historyService.addHistory(transaction);

    return transactionRepository.save(transaction);
  }

  public Transaction getById(Integer id) {
    return transactionRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("transaction ", 0));
  }

  public Transaction update(Integer id, Transaction transaction) {
    Transaction checkingTransaction = this.getById(id);
    transaction.setId(id);
    historyService.addHistory(checkingTransaction);
    return transactionRepository.save(transaction);
  }

}
