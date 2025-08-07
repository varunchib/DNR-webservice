package com.dnr.erp.modules.offerletter.service;

import com.dnr.erp.modules.offerletter.dto.OfferLetterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OfferLetterService {

    private final SimpleJdbcCall createOfferProc;
    private final SimpleJdbcCall getOfferProc; // ✅ New field
    private final ObjectMapper objectMapper;
    
    private UUID getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return UUID.fromString(auth.getName());
        }
        throw new RuntimeException("Unauthorized: Cannot extract user ID");
    }

    public OfferLetterService(DataSource dataSource, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        // ✅ Procedure to create offer letter
        this.createOfferProc = new SimpleJdbcCall(dataSource)
                .withSchemaName("dnrcore")
                .withProcedureName("prr_create_offer_letter")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_i_flag", Types.CHAR),
                        new SqlParameter("p_i_offer_letter_id", Types.OTHER), // UUID
                        new SqlParameter("p_i_candidate_name", Types.VARCHAR),
                        new SqlParameter("p_i_position", Types.VARCHAR),
                        new SqlParameter("p_i_ctc", Types.VARCHAR),
                        new SqlParameter("p_i_joining_date", Types.DATE),
                        new SqlParameter("p_i_salary_breakdown", Types.OTHER), // JSONB
                        new SqlParameter("p_i_created_by", Types.OTHER),
                        new SqlOutParameter("p_json_result", Types.OTHER)
                );

        // ✅ Procedure to fetch offer letter
        this.getOfferProc = new SimpleJdbcCall(dataSource)
                .withSchemaName("dnrcore")
                .withProcedureName("prr_call_get_offer_letter")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_i_offer_letter_id", Types.OTHER),
                        new SqlParameter("p_i_created_by", Types.OTHER),
                        new SqlOutParameter("p_json_result", Types.OTHER)
                );
    }

    public Map<String, Object> createOfferLetter(OfferLetterRequest request) {
        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("p_i_flag", "N");
            inParams.put("p_i_offer_letter_id", null);
            inParams.put("p_i_candidate_name", request.getEmployeeName());
            inParams.put("p_i_position", request.getPosition());
            inParams.put("p_i_ctc", request.getSalary());
            inParams.put("p_i_joining_date", new java.sql.Date(request.getJoiningDate().getTime()));
            inParams.put("p_i_salary_breakdown", objectMapper.writeValueAsString(request.getSalaryBreakdown()));
            inParams.put("p_i_created_by", getLoggedInUserId());

            Map<String, Object> result = createOfferProc.execute(inParams);
            Object jsonResult = result.get("p_json_result");
            
            return objectMapper.readValue(jsonResult.toString(), Map.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to call prr_create_offer_letter: " + e.getMessage(), e);
        }
    }

    // ✅ New method to fetch offer letter details by ID
    public JsonNode getOfferLetterById(UUID offerLetterId, UUID createdBy) {
        try {
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("p_i_offer_letter_id", offerLetterId);
            inParams.put("p_i_created_by", createdBy);

            Map<String, Object> result = getOfferProc.execute(inParams);
            Object jsonResult = result.get("p_json_result");

            return objectMapper.readTree(jsonResult.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to call prr_call_get_offer_letter: " + e.getMessage(), e);
        }
    }
}
