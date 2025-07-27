package com.dnr.erp.modules.bills.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnr.erp.modules.bills.dto.BillRequest;
import com.dnr.erp.modules.bills.service.BillService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/create")
    public ResponseEntity<JsonNode> createBill(@RequestBody BillRequest request) {
        request.setFlag("N");
        JsonNode response = billService.saveOrUpdateBill(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<JsonNode> updateBill(@RequestBody BillRequest request) {
        request.setFlag("U");
        JsonNode response = billService.saveOrUpdateBill(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get")
    public ResponseEntity<JsonNode> getBillById(@RequestBody BillRequest request) {
        JsonNode response = billService.getBillById(request.getId(), request.getCreatedBy());
        return ResponseEntity.ok(response);
    }

}
