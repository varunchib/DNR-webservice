package com.dnr.erp.modules.quotation.controller;

import com.dnr.erp.modules.quotation.dto.QuotationFilterRequest;
import com.dnr.erp.modules.quotation.dto.QuotationRequest;
import com.dnr.erp.modules.quotation.service.QuotationService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

	@Autowired
    private QuotationService service;

    public QuotationController(QuotationService service) {
        this.service = service;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<JsonNode> createQuotation(@RequestBody QuotationRequest request) {
        JsonNode result = service.createQuotation(request);
        return ResponseEntity.ok(result);
    }

    // 1. Get paginated quotations (no filters required)
    @PostMapping("/paginated")
    public ResponseEntity<JsonNode> getPaginated(@RequestBody QuotationFilterRequest request) {
        return ResponseEntity.ok(service.getQuotations(request));
    }

    // 2. Get full quotation by quotationId
    @PostMapping("/by-id")
    public ResponseEntity<JsonNode> getById(@RequestBody QuotationFilterRequest request) {
        return ResponseEntity.ok(service.getQuotations(request));
    }

    // 3. Get quotations by createdBy user (with pagination)
    @PostMapping("/by-user")
    public ResponseEntity<JsonNode> getByUser(@RequestBody QuotationFilterRequest request) {
        return ResponseEntity.ok(service.getQuotations(request));
    }
}
