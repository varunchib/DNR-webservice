package com.dnr.erp.modules.quotation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuotationRequestDto {
    private String email;
    private String emailBody;       // auto-generated from frontend
    private String columnsJson;     // JSON.stringify(columns)
    private String tableDataJson;   // JSON.stringify(tableData)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getColumnsJson() {
		return columnsJson;
	}
	public void setColumnsJson(String columnsJson) {
		this.columnsJson = columnsJson;
	}
	public String getTableDataJson() {
		return tableDataJson;
	}
	public void setTableDataJson(String tableDataJson) {
		this.tableDataJson = tableDataJson;
	}
    
    
}
