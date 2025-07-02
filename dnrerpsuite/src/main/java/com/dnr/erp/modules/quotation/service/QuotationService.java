package com.dnr.erp.modules.quotation.service;

import com.dnr.erp.modules.quotation.dto.QuotationRequestDto;
import com.dnr.erp.modules.quotation.entity.Quotation;
import com.dnr.erp.modules.quotation.repository.QuotationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;

    @Autowired
    public QuotationService(QuotationRepository quotationRepository) {
        this.quotationRepository = quotationRepository;
    }

    public Quotation createQuotation(QuotationRequestDto request) {
        Quotation quotation = new Quotation();
        quotation.setEmail(request.getEmail());
        quotation.setEmailBody(request.getEmailBody());
        quotation.setColumnsJson(request.getColumnsJson());
        quotation.setTableDataJson(request.getTableDataJson());

        return quotationRepository.save(quotation);
    }

    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    public Quotation getQuotationById(Long id) {
        return quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
    }
}
