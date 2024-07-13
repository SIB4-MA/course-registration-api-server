package com.registration.course.serverapp.api.transaction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.registration.course.serverapp.api.course.Course;
import com.registration.course.serverapp.api.course.CourseService;
import com.registration.course.serverapp.api.dto.request.EmailRequest;
import com.registration.course.serverapp.api.dto.request.TransactionRequest;
import com.registration.course.serverapp.api.dto.request.TransactionStatusAndIsRegisteredRequest;
import com.registration.course.serverapp.api.email.EmailSenderService;
import com.registration.course.serverapp.api.member.Member;
import com.registration.course.serverapp.api.member.MemberService;
import com.registration.course.serverapp.api.transaction.history.HistoryService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;

//  @Autowired
//  private ModelMapper modelMapper;

  @Autowired
  private CourseService courseService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private HistoryService historyService;

  @Autowired
  private EmailSenderService emailSenderService;

  public List<Transaction> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    return transactionRepository.findAll(sort);
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
    transaction.setCreatedAt(timestamp);

    // set status default -> enum SUCCESS, PROCESS, FAIL
    transaction.setStatus(TransactionStatus.PROCESS);

    Transaction transaction2 = transactionRepository.save(transaction);

    historyService.addHistory(transaction2);

    return transaction2;
  }

  public Transaction getById(Integer id) {
    return transactionRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("transaction ", 0));
  }

  public Transaction update(Integer id,
      TransactionStatusAndIsRegisteredRequest transactionStatusAndIsRegisteredRequest) {
    Transaction checkingTransaction = this.getById(id);
    EmailRequest emailRequest = new EmailRequest();

    checkingTransaction.setId(id);

    if (transactionStatusAndIsRegisteredRequest.getStatusUpdate().equalsIgnoreCase("Success")) {
      checkingTransaction.setStatus(TransactionStatus.SUCCESS);
      checkingTransaction.setIsRegistered(true);

      // send email lulus
      emailRequest.setSubject("RUANG KELAS");
      emailRequest.setTo(checkingTransaction.getMember().getEmail());
      emailSenderService.sendEmailTemplate(emailRequest, true);

      // tambahkan count
      memberService.updateCourseActiveById(checkingTransaction.getMember().getId());

    } else if (transactionStatusAndIsRegisteredRequest.getStatusUpdate().equalsIgnoreCase("process")) {
      checkingTransaction.setStatus(TransactionStatus.PROCESS);
    } else if (transactionStatusAndIsRegisteredRequest.getStatusUpdate().equalsIgnoreCase("Failed")) {
      checkingTransaction.setStatus(TransactionStatus.FAILED);

      // send email gagal
      emailRequest.setSubject("RUANG KELAS");
      emailRequest.setTo(checkingTransaction.getMember().getEmail());
      emailSenderService.sendEmailTemplate(emailRequest, false);
    }

    historyService.addHistory(checkingTransaction);
    return transactionRepository.save(checkingTransaction);
  }

  public List<Transaction> getAllTransactionsByMemberId(Integer memberId) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    return transactionRepository.findAllByMemberId(memberId, sort);
  }

  public List<Transaction> getAllTransactionsByMemberIdSessionAndIsRegistered(Integer id) {
    return transactionRepository.findAllByMemberIdAndIsRegistered(id, true);
  }

}
