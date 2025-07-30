package com.dnr.erp.modules.bills.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnr.erp.modules.bills.dto.BillRequest;
import com.dnr.erp.modules.bills.service.BillService;
import com.dnr.erp.modules.quotation.service.PdfService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/bills")
public class BillController {

	private final PdfService pdfService;
    private final BillService billService;

    public BillController(PdfService pdfService, BillService billService) {
    	this.pdfService = pdfService;
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
    
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateHtmlPdf(@RequestBody Map<String, String> payload) throws IOException {
        String html = payload.get("html");

        if (html == null || html.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] pdfBytes = pdfService.generateHtmlToPdf(html);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("bill.pdf")
                                .build()
                                .toString())
                .body(pdfBytes);
    }

}
