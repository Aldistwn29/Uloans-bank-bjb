package com.bjb.uloans.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.bjb.uloans.event.LoanCreatedEvent;
import com.bjb.uloans.service.AuditLogSevice;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class AuditLogListener {
    private final AuditLogSevice auditLogService;
    @TransactionalEventListener (phase = TransactionPhase.AFTER_COMMIT)
    @Transactional 
    
    public void handle(LoanCreatedEvent event){
        auditLogService.recordLoanCreated(event);
    }
}