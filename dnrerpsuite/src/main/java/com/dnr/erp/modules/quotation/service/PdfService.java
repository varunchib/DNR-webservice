package com.dnr.erp.modules.quotation.service;

import com.dnr.erp.modules.quotation.dto.ColumnDto;
import com.dnr.erp.modules.quotation.dto.RowDto;
import com.dnr.erp.modules.quotation.entity.Quotation;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PdfService {

    public byte[] generateQuotationPdf(Quotation quotation, List<ColumnDto> columns, List<RowDto> rows) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        try {
            var logoFile = new ClassPathResource("static/images/logo.png");
            Image logo = new Image(ImageDataFactory.create(logoFile.getURL()))
                    .scaleToFit(120, 60)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(logo);
        } catch (IOException e) {
            System.err.println("⚠️ Logo not found: " + e.getMessage());
        }

        // Header
        Paragraph header = new Paragraph("QUOTATION")
                .simulateBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18);
        doc.add(header);
        doc.add(new Paragraph("\n"));

        // Quotation Info
        doc.add(new Paragraph("To: " + quotation.getAttention()));
        doc.add(new Paragraph("Designation: " + quotation.getDesignation()));
        doc.add(new Paragraph("Company: " + quotation.getCompanyName()));
        doc.add(new Paragraph("Email: " + quotation.getEmail()));
        doc.add(new Paragraph("Phone: " + quotation.getPhone()));
        doc.add(new Paragraph("Address: " + quotation.getAddress()));
        doc.add(new Paragraph("Website: " + quotation.getWebsite()));
        doc.add(new Paragraph("Subject: " + quotation.getSubject()));
        doc.add(new Paragraph("Project: " + quotation.getProject()));
        doc.add(new Paragraph("Date: " + quotation.getDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))));
        doc.add(new Paragraph("\n"));

        // Table
        int columnCount = columns.size();
        Table table = new Table(UnitValue.createPercentArray(columnCount))
                .useAllAvailableWidth();

        // Add header cells
        for (ColumnDto column : columns) {
            table.addHeaderCell(new Cell().add(new Paragraph(column.getColumnName()).simulateBold()));
        }

        // Add row cells
        for (RowDto row : rows) {
            Map<String, String> cells = row.getCells();
            for (ColumnDto column : columns) {
                String colId = column.getColumnId();
                String value = cells.getOrDefault(colId, "");
                table.addCell(new Cell().add(new Paragraph(value)));
            }
        }

        doc.add(table);
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("Authorized Signature:\n\n\n_________________________"));

        doc.close();
        return baos.toByteArray();
    }
}
