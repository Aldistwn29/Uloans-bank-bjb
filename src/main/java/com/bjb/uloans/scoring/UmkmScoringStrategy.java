package com.bjb.uloans.scoring;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bjb.uloans.model.Customer;
import com.bjb.uloans.model.LoanHistory;

@Component 
public class UmkmScoringStrategy implements ScoringStrategi {

    // Rumus skor dari history pinjaman + umur usaha + status BI/Amola
    // dipakai sebelum decission APPROVED/REJECTED
    @Override
    public int calculateScore(Customer customer, List<LoanHistory> histories, String bisStatus, String amloStatus){
        
        int score = 0;
        // jika tidak ada kredit macet/gagal di semua history
        boolean hasDefault = histories.stream().anyMatch(h -> "GAGAL".equals(h.getStatus()));
        if(!hasDefault){
            score += 40;
        }

        // jika usaha berjalan > 2 tahun, anggap lebih stabil
        if(customer.getBusinessAge() > 2){
            score += 20;
        }

        // 20+ jika minimal 2 pinjaman lama lunas, bukti bayar disiplin
        long paidOff = histories.stream().filter(h -> "LUNAS".equals(h.getStatus())).count();
        if(paidOff >= 2){
            score += 20;
        }

        if("CLEAR".equals(bisStatus) && "SAFE".equals(amloStatus)){
            score += 20;
        }

        // kunci score 0-100 agar kontrak interface tetap valid
        if(score > 100){
            return 100;
        }

        return score;
    }


}