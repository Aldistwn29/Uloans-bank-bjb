package com.bjb.uloans.service;


import com.bjb.uloans.model.ComplianceLog;
import com.bjb.uloans.model.Customer;
import com.bjb.uloans.model.LoanApplication;
import com.bjb.uloans.model.LoanHistory;
import com.bjb.uloans.model.LoanStatus;
import com.bjb.uloans.repository.CustomerRepository;
import com.bjb.uloans.repository.LoanApplicationRepository;
import com.bjb.uloans.repository.LoanHistoryRepository;
import com.bjb.uloans.scoring.ScoringStrategi;
import com.bjb.uloans.event.LoanCreatedEvent;

import jakarta.persistence.EntityNotFoundException;

import com.bjb.uloans.repository.ComplianceLogRepository;
import com.bjb.uloans.compliance.AmloService;
import com.bjb.uloans.compliance.BicheckingService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

// orchestrate pengajuan UMK: Cek BI + AMOLA, skor, simpan hasil dan brodcast event audit
@Service
@RequiredArgsConstructor
public class LoanApplicationService {
    private final CustomerRepository customerRepository;
    private final LoanHistoryRepository loanHistoryRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final ComplianceLogRepository complianceLogRepository;
    private final BicheckingService bicheckingService;
    private final AmloService amloService;
    private final ScoringStrategi scoringStrategi;
    private final ApplicationEventPublisher eventPublisher;


    // Jalankan 7 langkah apply dalam 1 transaksi, return aplikasi tersimpan untuk mapper ke DTO
    @Transactional
    public LoanApplication apply(String nik, long requestAmount, String username){
        Customer customer = customerRepository.findByNik(nik)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + nik));
        String biStatus = bicheckingService.checkBic(nik);
        String amloStatus = amloService.check(nik);

        List<LoanHistory> histories = loanHistoryRepository.findByCustomer(customer);
        int score = scoringStrategi.calculateScore(customer,histories, biStatus, amloStatus);

        // guard Blacklist agar NIK selalu REJECTED walaupun history bagus
        if("BLACKLIST".equals(biStatus)){
            score = 0;
        }

        LoanStatus decision = decide(score);
        long recommendedAmount = recommed(requestAmount, score);
        LocalDateTime now = LocalDateTime.now();

        LoanApplication saved = loanApplicationRepository.save(LoanApplication.builder()
            .customer(customer)
            .requestedAmount(requestAmount)
            .amloStatus(amloStatus)
            .biStatus(biStatus)
            .finalScore(score)
            .decision(decision)
            .recommendedAmount(recommendedAmount)
            .createdBy(username)
            .createdAt(now)
            .build()
        );

        // jejak cek kepatuhan per pengajuan
        complianceLogRepository.save(ComplianceLog.builder()
            .application(saved)
            .nik(nik)
            .biStatus(biStatus)
            .amloStatus(amloStatus)
            .checkedBy(username)
            .checkedAt(now)
            .build()
        );

        eventPublisher.publishEvent(new LoanCreatedEvent(saved, username));
        return saved;
    } 

    // petakkan skor decission : >=80 APPROVED, 69-70 APPROVED, <60 ReJECTED
    private LoanStatus decide(int score){
        if(score >= 60){
            return LoanStatus.APPROVED;
        }
        return LoanStatus.REJECTED;
    }

    // hitung nominal disetujui
    private long recommed(long requestedAmount, int score){
        if(score >= 80){
            return requestedAmount;
        }
        if(score >= 60){
            return requestedAmount * 70 / 100;
        }
        return 0L;
    }
} 