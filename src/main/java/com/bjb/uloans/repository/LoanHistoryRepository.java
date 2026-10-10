package com.bjb.uloans.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.Customer;
import com.bjb.uloans.model.LoanHistory;

// get history loan for input UKMScoringStrategi
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    List<LoanHistory> findByCustomer(Customer customer);
}