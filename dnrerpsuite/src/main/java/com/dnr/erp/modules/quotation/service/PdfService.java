//package com.dnr.erp.modules.quotation.service;
//
//import com.dnr.erp.modules.quotation.entity.Quotation;
//import com.dnr.erp.modules.quotation.dto.Row;
//import com.itextpdf.io.source.ByteArrayOutputStream;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.*;
//import com.itextpdf.layout.property.TextAlignment;
//import com.itextpdf.layout.property.UnitValue;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PdfService {
//
//    public byte[] generateQuotationPdf(Quotation quotation, List<Row> rows) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document doc = new Document(pdf);
//
//        // Header
//        Paragraph header = new Paragraph("Quotation")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setBold()
//                .setFontSize(18);
//        doc.add(header);
//
//        // Quotation Info
//        doc.add(new Paragraph("Quotation ID: " + quotation.getId()));
//        doc.add(new Paragraph("Client: " + quotation.getClientName()));
//        doc.add(new Paragraph("Date: " + quotation.getCreatedAt())); // format as needed
//        doc.add(new Paragraph("\n"));
//
//        // Table
//        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 4, 4}))
//                .useAllAvailableWidth();
//
//        table.addHeaderCell("Item");
//        table.addHeaderCell("Description");
//        table.addHeaderCell("Amount");
//
//        for (Row row : rows) {
//            table.addCell(row.getColumn1());
//            table.addCell(row.getColumn2());
//            table.addCell(row.getColumn3());
//        }
//
//        doc.add(table);
//
//        // Footer
//        doc.add(new Paragraph("\n\nSignature: ______________________"));
//
//        doc.close();
//        return baos.toByteArray();
//    }
//}
