package com.bjb.uloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.ComplianceLog;

// save history BI + Amola 
public interface ComplianceLogRepository extends JpaRepository<ComplianceLog, Long>{
    
}
