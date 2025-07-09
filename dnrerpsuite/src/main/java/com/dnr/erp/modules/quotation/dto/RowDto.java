package com.dnr.erp.modules.quotation.dto;

import java.util.Map;

public class RowDto {
	private int rowIndex;
    private Map<String, String> cells;
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public Map<String, String> getCells() {
		return cells;
	}
	public void setCells(Map<String, String> cells) {
		this.cells = cells;
	}
    
    
}
