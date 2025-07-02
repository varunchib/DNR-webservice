package com.dnr.erp.modules.quotation.repository;

import com.dnr.erp.modules.quotation.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}
