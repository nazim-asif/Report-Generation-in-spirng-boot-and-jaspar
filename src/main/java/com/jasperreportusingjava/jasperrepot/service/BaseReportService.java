package com.jasperreportusingjava.jasperrepot.service;

import com.jasperreportusingjava.jasperrepot.configuration.CsvReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.DocxReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.PdfReportConfiguration;
import com.jasperreportusingjava.jasperrepot.configuration.XlsxReportConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nazim Uddin Asif
 * @since 07/Apr/2021
 */
@Service
public class BaseReportService {

    public ResponseEntity<byte[]> generateReport(){
        //feature 1
//        Map<String, Object> data = new HashMap<>();
//        PdfReportConfiguration pdfReportConfiguration = new PdfReportConfiguration();
//        pdfReportConfiguration.setTemplateName("jrxml formates/Employees.jrxml");
//        pdfReportConfiguration.setReportName("employee");
//        pdfReportConfiguration.setPassword("1234"); //feature 2
//        return Report.generatePdf(data, pdfReportConfiguration);

//        DocxReportConfiguration docxReportConfiguration = new DocxReportConfiguration();
//        docxReportConfiguration.setTemplateName("jrxml formates/Employees.jrxml");
//        docxReportConfiguration.setReportName("employee");
//        return Report.generateDocx(data, docxReportConfiguration);

//        CsvReportConfiguration csvReportConfiguration = new CsvReportConfiguration();
//        csvReportConfiguration.setTemplateName("jrxml formates/Employees.jrxml");
//        csvReportConfiguration.setReportName("employee");
//        return Report.generateCsv(data, csvReportConfiguration);

//        XlsxReportConfiguration xlsxReportConfiguration = new XlsxReportConfiguration();
//        xlsxReportConfiguration.setTemplateName("jrxml formates/Employees.jrxml");
//        xlsxReportConfiguration.setReportName("employee");
//        return Report.generateXlsx(data, xlsxReportConfiguration);

        //Feature 3

        XlsxReportConfiguration xlsxReportConfiguration = new XlsxReportConfiguration();
        xlsxReportConfiguration.setTemplateName("jrxml formates/Employees.jrxml");
        xlsxReportConfiguration.setReportName("employee");

        ArrayList<ArrayList<String>> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> allData = new ArrayList<>();
        ArrayList<String> sheets = new ArrayList<>();

        ArrayList<String> header1 = new ArrayList<>();
        header1.add("Header1");
        header1.add("Header2");
        header1.add("Header3");
        headers.add(header1);

        ArrayList<String> header2 = new ArrayList<>();
        header2.add("Header1");
        header2.add("Header2");
        header2.add("Header3");
        headers.add(header2);


        ArrayList<String> data1 = new ArrayList<>();
        data1.add("value1");
        data1.add("value2");
        data1.add("value3");
        data1.add("value4");
        allData.add(data1);

        ArrayList<String> data2 = new ArrayList<>();
        data2.add("value1");
        data2.add("value2");
        data2.add("value3");
        data2.add("value4");
        data2.add("value5");
        allData.add(data2);

        sheets.add("sheet1");
        sheets.add("sheet2");
        return Report.generateXlsx(sheets,headers,allData,xlsxReportConfiguration);
    }
}
