package com.bjb.uloans.compliance;

import org.springframework.stereotype.Service;

@Service 
public class MockAmolaService implements AmloService {
    
    @Override
    public String check(String nik){
        if("0000000000000000".equals(nik)){
            return "UNSAFE";
        }
        return "SAFE";
    } 
}
