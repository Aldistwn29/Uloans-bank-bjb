package com.bjb.uloans.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjb.uloans.model.LoanApplication;
import com.bjb.uloans.dto.LoanApplicationRequest;
import com.bjb.uloans.dto.LoanApplicationResponse;
import com.bjb.uloans.mapper.LoanApplicationMapper;
import com.bjb.uloans.repository.LoanApplicationRepository;
import com.bjb.uloans.service.LoanApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/loan-applications")
@RequiredArgsConstructor 
public class LoanApplicationController {
    private final LoanApplicationService service;
    private final LoanApplicationRepository repository;

    @PostMapping
    public LoanApplicationResponse apply(@Valid  @RequestBody  LoanApplicationRequest req){
        LoanApplication saved = service.apply(req.nik(), req.requestedAmount(), "analyst");
        return LoanApplicationMapper.toResponse(saved);
    }

    @GetMapping 
    public List<LoanApplicationResponse> last10() {
        return repository.findTop10ByOrderByIdDesc().stream().map(LoanApplicationMapper::toResponse).toList();
    }
}
