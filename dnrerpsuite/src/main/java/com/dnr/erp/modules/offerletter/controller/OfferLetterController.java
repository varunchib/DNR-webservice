package com.dnr.erp.modules.offerletter.controller;

import com.dnr.erp.modules.offerletter.dto.OfferLetterPdfRequest;
import com.dnr.erp.modules.offerletter.dto.OfferLetterRequest;
import com.dnr.erp.modules.offerletter.service.OfferLetterService;
import com.dnr.erp.modules.quotation.service.PdfService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/offer-letters")
public class OfferLetterController {

    private final OfferLetterService offerLetterService;
    private final PdfService pdfService;

    public OfferLetterController(OfferLetterService offerLetterService, PdfService pdfService) {
        this.offerLetterService = offerLetterService;
        this.pdfService = pdfService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOfferLetter(@RequestBody OfferLetterRequest request) {
        try {
            Map<String, Object> result = offerLetterService.createOfferLetter(request);

            if ("F".equals(result.get("resultStatus"))) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateHtmlPdf(@RequestBody OfferLetterPdfRequest payload) throws IOException {
        String html = payload.getHtml();

        if (html == null || html.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] pdfBytes = pdfService.generateHtmlToPdf(html);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("offer-letter.pdf")
                                .build()
                                .toString())
                .body(pdfBytes);
    }
    
    @PostMapping("/get")
    public ResponseEntity<?> getOfferLetterDetails(@RequestBody Map<String, Object> body) {
        try {
            UUID offerLetterId = UUID.fromString(body.get("offerLetterId").toString());
            UUID createdBy = body.get("createdBy") != null
                    ? UUID.fromString(body.get("createdBy").toString())
                    : null;

            JsonNode result = offerLetterService.getOfferLetterById(offerLetterId, createdBy);

            if (result == null || result.get("resultStatus").asText().equals("F")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
