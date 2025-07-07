package com.dnr.erp.modules.quotation.service;

import com.dnr.erp.modules.quotation.config.StoredProcClient;
import com.dnr.erp.modules.quotation.dto.QuotationFilterRequest;
import com.dnr.erp.modules.quotation.dto.QuotationRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QuotationService {

    private final StoredProcClient procClient;

    public QuotationService(StoredProcClient procClient) {
        this.procClient = procClient;
    }
    
    public UUID getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof String userIdStr) {
            try {
                return UUID.fromString(userIdStr);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Invalid userId in principal");
            }
        }

        throw new RuntimeException("Invalid user context");
    }
    
    public String getLoggedInUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            return auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst() // Assume one role
                    .map(role -> role.replace("ROLE_", ""))
                    .orElse(null);
        }
        throw new RuntimeException("Role not found");
    }



    public JsonNode getQuotations(QuotationFilterRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_i_page", request.getPage() != null ? request.getPage() : 0);
        params.put("p_i_size", request.getSize() != null ? request.getSize() : 5);
        params.put("p_i_reference_no", request.getReferenceNo());
        params.put("p_i_status", request.getStatus());
        params.put("p_i_created_by", request.getCreatedBy());
        params.put("p_i_quotation_id", request.getQuotationId());

        return procClient.callPaginatedProc("prr_get_paginated_quotations", params);
    }

    public JsonNode createQuotation(QuotationRequest request) {
        
        UUID userId = getLoggedInUserId();

        try {
            Map<String, Object> params = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();

            params.put("p_i_reference_no", safe(request.getReferenceNo()));
            params.put("p_i_date", new java.sql.Date(request.getDate().getTime()));
            params.put("p_i_company_name", safe(request.getCompanyName()));
            params.put("p_i_attention", safe(request.getAttention()));
            params.put("p_i_designation", safe(request.getDesignation()));
            params.put("p_i_email", safe(request.getEmail()));
            params.put("p_i_phone", safe(request.getPhone()));
            params.put("p_i_address", safe(request.getAddress()));
            params.put("p_i_website", safe(request.getWebsite()));
            params.put("p_i_subject", safe(request.getSubject()));
            params.put("p_i_project", safe(request.getProject()));
            params.put("p_i_columns", mapper.writeValueAsString(request.getColumns())); // ✅ JSON -> string
            params.put("p_i_rows", mapper.writeValueAsString(request.getRows()));  
            params.put("p_i_created_by", userId);

            return procClient.callCreateQuotationProc("prr_create_quotation", params);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create quotation: " + e.getMessage(), e);
        }
    }
    
    private String safe(String input) {
        return input != null ? input : "";
    }
}


