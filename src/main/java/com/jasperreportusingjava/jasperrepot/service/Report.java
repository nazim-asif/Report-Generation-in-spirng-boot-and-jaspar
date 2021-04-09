package com.jasperreportusingjava.jasperrepot.service;

import com.jasperreportusingjava.jasperrepot.configuration.CsvReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.DocxReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.PdfReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.XlsxReportConfiguration;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Nazim Uddin Asif
 * @since 1.0
 */
public class Report {
    /**
     * Generate PDF
     *
     * @param data                template configuration data
     * @param reportConfiguration PdfReportConfiguration
     * @return pdf file by array of byte
     */
    public static ResponseEntity<byte[]> generatePdf(Map<String, Object> data, PdfReportConfiguration reportConfiguration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(configureJasperTemplate(data, reportConfiguration.getTemplateName())));

        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        if (reportConfiguration.getPassword() != null) {
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setEncrypted(true);
            configuration.set128BitKey(true);
            configuration.setUserPassword(reportConfiguration.getPassword());
            //configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
            exporter.setConfiguration(configuration);
        }
        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }


        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + reportConfiguration.getReportName() + ".pdf\"")
                .body(outputStream.toByteArray());
    }

    /**
     * Generate Docx
     *
     * @param data                template configuration data
     * @param reportConfiguration DocxReportConfiguration
     * @return docx file by array of byte
     */

    public static ResponseEntity<byte[]> generateDocx(Map<String, Object> data, DocxReportConfiguration reportConfiguration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(configureJasperTemplate(data, reportConfiguration.getTemplateName())));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + reportConfiguration.getReportName() + ".docx\"")
                .body(outputStream.toByteArray());
    }

    /**
     * Generate CSV
     *
     * @param data                template configuration data
     * @param reportConfiguration CsvReportConfiguration
     * @return csv file by array of byte
     */
    public static ResponseEntity<byte[]> generateCsv(Map<String, Object> data, CsvReportConfiguration reportConfiguration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(configureJasperTemplate(data, reportConfiguration.getTemplateName())));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));

        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type", "text/csv; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + reportConfiguration.getReportName() + ".csv\"")
                .body(outputStream.toByteArray());
    }

    /**
     * Generate Xlsx File
     *
     * @param data                template configuration data
     * @param reportConfiguration XlsxReportConfiguration
     * @return Xlsx file of array of byte
     */
    public static ResponseEntity<byte[]> generateXlsx(Map<String, Object> data, XlsxReportConfiguration reportConfiguration) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(configureJasperTemplate(data, reportConfiguration.getTemplateName())));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        try {

            exporter.exportReport();

        } catch (JRException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + reportConfiguration.getReportName() + ".xlsx\"")
                .body(outputStream.toByteArray());
    }


    /**
     * Generate Xlsx File With Number Of Sheet
     * @param sheets List of sheet name
     * @param headers multiple amounts of List of Header name
     * @param allData multiple amounts of list of data
     * @param reportConfiguration XlsxReportConfiguration
     * @return Xlsx file by array of byte
     */
    public static ResponseEntity<byte[]> generateXlsx(List<String> sheets, ArrayList<ArrayList<String>> headers, ArrayList<ArrayList<String>> allData, XlsxReportConfiguration reportConfiguration){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        for (int item = 0; item < sheets.size(); item++) {
            Sheet sheet = workbook.createSheet(sheets.get(item));
            loadDataInExcelFile(sheet, headers.get(item), allData.get(item));
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/octet-stream; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + reportConfiguration.getReportName() + ".xlsx\"")
                .body(outputStream.toByteArray());
    }

    /**
     * Fill data into Report
     *
     * @param data          template configuration data
     * @param jrxmlFilePath jrxml file containing path
     * @return filled template
     */
    private static JasperPrint configureJasperTemplate(Map<String, Object> data, String jrxmlFilePath) {
        File file;
        JasperReport Report;
        JasperPrint jasperPrint = null;
        try {
            file = ResourceUtils.getFile("classpath:" + jrxmlFilePath);
            Report = JasperCompileManager.compileReport(file.getAbsolutePath());
            jasperPrint = JasperFillManager.fillReport(Report, data, new JREmptyDataSource());
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }

        return jasperPrint;
    }

    /**
     * Fill up every sheet using headers and data
     * @param sheet sheet name
     * @param header list of header
     * @param data list of data
     */

    private static void loadDataInExcelFile(Sheet sheet, ArrayList<String> header, ArrayList<String> data) {
        int headerAmount = header.size(),
                dataAmount = data.size(),
                check = dataAmount % headerAmount,
                k = 0;

        if (check == 0)
            dataAmount = check;
        else
            dataAmount = check + 1;

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerAmount; i++) {
            headerRow.createCell(i).setCellValue(header.get(i));
        }

        for (int i = 0; i < dataAmount; i++) {
            Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < headerAmount; j++) {
                if (k < data.size())
                    dataRow.createCell(j).setCellValue(data.get(k));
                else
                    dataRow.createCell(j).setCellValue("");
                k++;
            }
        }

    }
}