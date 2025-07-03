package com.dnr.erp.modules.quotation.controller;

import com.dnr.erp.modules.quotation.dto.QuotationRequestDto;
import com.dnr.erp.modules.quotation.service.QuotationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quotation")
public class QuotationController {

    private final QuotationService quotationService;

    @Autowired
    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    public ResponseEntity<?> createQuotation(@RequestBody QuotationRequestDto request) {
        return ResponseEntity.ok(quotationService.createQuotation(request));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getQuotationById(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }
}
