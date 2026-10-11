package com.bjb.uloans.scoring;

import com.bjb.uloans.model.Customer;
import com.bjb.uloans.model.LoanHistory;
import java.util.List;

public interface ScoringStrategi {
    // hitung skor 0-100 dipakai service scoring untuk menilai kelayakan pinjaman
    int calculateScore(Customer customer, List<LoanHistory> histories, String biStatus, String amloStatus);
}
