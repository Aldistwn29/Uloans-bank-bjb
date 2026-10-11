package com.bjb.uloans.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record LoanApplicationRequest(
    // Nik 16 digits
    @Pattern(regexp = "\\d{16}", message = "Nik must be 16 digits")
    String nik,
    
    // Nominal diajukkan dalam rupiah; long, harus > 0
    @Positive (message = "Requested must be > 0")
    long requestedAmount
) {
    
}
