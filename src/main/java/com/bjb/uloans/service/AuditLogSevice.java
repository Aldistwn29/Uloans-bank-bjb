package com.bjb.uloans.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bjb.uloans.event.LoanCreatedEvent;
import com.bjb.uloans.model.AuditLog;
import com.bjb.uloans.repository.AuditLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuditLogSevice {

    private final AuditLogRepository auditLogRepository;
    @Transactional 


    public void recordLoanCreated(LoanCreatedEvent event){
        if(event == null || event.application() == null){
            return;
        }

        auditLogRepository.save(AuditLog.builder()
            .username(event.username())
            .action("LOAN_CREATED")
            .entityId(event.application().getId())
            .timestamp(LocalDateTime.now())
            .build());
    }
}
