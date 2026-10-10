package com.bjb.uloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.LoanApplication;


// save result apply
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    
}
