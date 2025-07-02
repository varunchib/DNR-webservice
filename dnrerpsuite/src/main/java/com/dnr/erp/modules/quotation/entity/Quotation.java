package com.dnr.erp.modules.quotation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quotations", schema = "dnrcore")
@Getter @Setter
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "email_body", columnDefinition = "TEXT")
    private String emailBody;

    @Column(name = "columns_json", columnDefinition = "TEXT")
    private String columnsJson;

    @Column(name = "table_data_json", columnDefinition = "TEXT")
    private String tableDataJson;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
