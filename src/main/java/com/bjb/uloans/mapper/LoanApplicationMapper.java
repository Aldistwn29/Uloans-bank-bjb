package com.bjb.uloans.mapper;


import com.bjb.uloans.model.LoanApplication;
import com.bjb.uloans.dto.LoanApplicationResponse;

public class LoanApplicationMapper {
    // kunci instansi karena semua method static ulity
    private LoanApplicationMapper(){

    }

    /**
     * Ubah hasil save service menjadi kontrak API; Bangun reason manusiawi skor dan status
     */
    public static LoanApplicationResponse toResponse(LoanApplication app){
        return new LoanApplicationResponse(
            app.getDecision(),
            app.getFinalScore(),
            buildReason(app),
            app.getRecommendedAmount(),
            app.getBiStatus(),
            app.getAmloStatus()
        );
    }

    // susun alasan: blokir BI Prioritas utama
    private static String buildReason(LoanApplication app){
        if("BLACKLIST".equals(app.getBiStatus())) {
            return "Ditolak: Nik diblokir BI Checking";
        }
        if(app.getFinalScore() >= 80){
            return "Disetujui: skor " + app.getFinalScore() + "  layak penuh";
        }
        if(app.getFinalScore() >= 60){
            return "Disetujui 70%: skor " + app.getFinalScore() + " layak sebagian";
        }
        return "Ditolak: skor " + app.getFinalScore() + " di bawah minimum";
    }
}
