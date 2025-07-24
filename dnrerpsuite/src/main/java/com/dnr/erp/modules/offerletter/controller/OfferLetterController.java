package com.dnr.erp.modules.offerletter.controller;

import com.dnr.erp.modules.quotation.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/offer-letters")
public class OfferLetterController {

    private final PdfService pdfService;

    public OfferLetterController(PdfService pdfService) {
        this.pdfService = pdfService;
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
                                .filename("offer-letter.pdf")
                                .build()
                                .toString())
                .body(pdfBytes);
    }
}
