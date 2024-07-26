package com.registration.course.serverapp.api.transaction.history;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.transaction.Transaction;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HistoryService {

  @Autowired
  private HistoryRepository historyRepository;

  public List<History> getAll() {
    Sort sort = Sort.by(Sort.Direction.DESC, "date");
    return historyRepository.findAll(sort);
  }

  public History addHistory(Transaction transaction) {
    History history = new History();

    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
    history.setDate(timestamp);

    history.setStatus(transaction.getStatus());
    history.setTransaction(transaction);

    return historyRepository.save(history);
  }

  public List<History> getAllHistoriesByMemberId(Integer memberId) {
    Sort sort = Sort.by(Sort.Direction.DESC, "date");
    return historyRepository.findAllByTransactionMemberId(memberId, sort);
  }

}
