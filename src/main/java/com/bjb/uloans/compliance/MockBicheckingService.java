package com.bjb.uloans.compliance;

import org.springframework.stereotype.Service;


@Service 
public class MockBicheckingService implements  BicheckingService{
    @Override
    public String checkBic(String nik){
        if("0000000000000000".equals(nik)){
            return "BLACKLIST";
        }
        
        int lastDigit = nik.charAt(nik.length() - 1) - '0';
        if(lastDigit % 2 == 1){
            return "HAS_DEFAULT";
        }

        return "CLEAR";
    }
}
