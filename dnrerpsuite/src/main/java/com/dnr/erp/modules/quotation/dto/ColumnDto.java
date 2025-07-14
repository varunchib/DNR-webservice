package com.dnr.erp.modules.quotation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnDto {
	
	@JsonProperty("id")
	private String id;


    @JsonProperty("column_id")  
    private String columnId;

    @JsonProperty("column_name") 
    private String columnName;

    private String visible;
    
    @JsonProperty("quotation_id")
    private String quotationId;   
    

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}
    
    
}
