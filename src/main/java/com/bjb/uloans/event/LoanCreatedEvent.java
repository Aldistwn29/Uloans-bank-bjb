package com.bjb.uloans.event;

import com.bjb.uloans.model.LoanApplication;


public record LoanCreatedEvent(
    // Aplikasi baru disimpan, sumber id + nik untuk audit
    LoanApplication application,
    // Analyst pemroses, ditulis ke audit_logs.username
    String username
) {
     
}
