//package com.dnr.erp.modules.quotation.dto;
//
//import com.dnr.erp.modules.quotation.entity.Quotation;
//
//public record QuotationResponseDto(Long id, String email, LocalDateTime generatedAt) {}
//
//public QuotationResponseDto createQuotation(QuotationRequestDto request) {
//    Quotation q = new Quotation();
//    q.setEmail(request.getEmail());
//    q.setEmailBody(request.getEmailBody());
//    q.setColumnsJson(request.getColumnsJson());
//    q.setTableDataJson(request.getTableDataJson());
//    q.setGeneratedAt(LocalDateTime.now());
//
//    Quotation saved = quotationRepository.save(q);
//    return new QuotationResponseDto(saved.getId(), saved.getEmail(), saved.getGeneratedAt());
//}
//
//
