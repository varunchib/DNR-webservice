package com.dnr.erp.modules.quotation.dto;

import java.util.UUID;

public class QuotationFilterRequest {
    public Integer page = 0;
    public Integer size = 5;
    public String referenceNo;
    public String status;
    public UUID createdBy;
    public UUID quotationId;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UUID getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UUID createdBy) {
		this.createdBy = createdBy;
	}
	public UUID getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(UUID quotationId) {
		this.quotationId = quotationId;
	}
        
}
