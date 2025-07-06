package com.dnr.erp.modules.quotation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quotations", schema = "dnrcore")

@Builder
public class Quotation {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "attention")
    private String attention;

    @Column(name = "designation")
    private String designation;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "subject")
    private String subject;

    @Column(name = "project")
    private String project;

    @Column(name = "table_data")
    private String tableData; // If you're storing raw JSON as text

    @Column(name = "created_by", columnDefinition = "UUID")
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "status")
    private String status;
}
