package com.bjb.uloans.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.LoanApplication;


// save result apply
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findTop10ByOrderByIdDesc();
}
