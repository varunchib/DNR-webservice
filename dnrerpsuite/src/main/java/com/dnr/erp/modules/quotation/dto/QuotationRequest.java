package com.dnr.erp.modules.quotation.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuotationRequest {
    private String referenceNo;
    private Date date;
    private String companyName;
    private String attention;
    private String designation;
    private String email;
    private String phone;
    private String address;
    private String website;
    private String subject;
    private String project;
    private List<Map<String, Object>> columns;
    private List<Map<String, Object>> rows;
    private UUID createdBy;
    private String role;

    
    public QuotationRequest() {}


	public String getReferenceNo() {
		return referenceNo;
	}


	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getAttention() {
		return attention;
	}


	public void setAttention(String attention) {
		this.attention = attention;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getProject() {
		return project;
	}


	public void setProject(String project) {
		this.project = project;
	}


	public List<Map<String, Object>> getColumns() {
		return columns;
	}


	public void setColumns(List<Map<String, Object>> columns) {
		this.columns = columns;
	}


	public List<Map<String, Object>> getRows() {
		return rows;
	}


	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}


	public UUID getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(UUID createdBy) {
		this.createdBy = createdBy;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

    
}
