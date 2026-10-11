package com.bjb.uloans.dto;

import com.bjb.uloans.model.LoanStatus;

public record LoanApplicationResponse(
    // Hasil akhir APPROVED/REJECTED dari service.decide()
    LoanStatus decision,
    // Skor 0-100 dari ScoringStrategi
    int finalScore,
    String reason,
    // disetujui atau tidak
    long recommendedAmount,
    // status BI
    String biStatus,
    // status Amlo
    String amloStatus
    
){

}