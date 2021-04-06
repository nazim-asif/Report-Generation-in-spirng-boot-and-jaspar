package com.jasperreportusingjava.jasperrepot.service;


import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;

import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pojo.TableStructure;


import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Nazim Uddin Asif
 * @since 28/Mar/2021
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;

    /**
     * This method is customizing the data for jrxml file.
     *
     * @param map it takes the value of jrxmlFilePath,
     *            reportFormat,reportName and destinationPath from user
     * @return It returns destination path of downloaded file
     * @throws FileNotFoundException
     * @throws JRException
     */

    public String getCustomisedDataForReport(Map map) throws IOException, JRException, GeneralSecurityException {
        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("name", "Nazim Uddin Asif");
//        parameters.put("reportTitle", "Employee Info");
//        parameters.put("designation", "Manager");
//        parameters.put("reportDescription", "Employee Information Report");
//
//        List<Employee> employees = repository.findAll();
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
//        parameters.put("tblData", dataSource);
        generateReport(map, parameters);

//        ArrayList<ArrayList<String>> headers = new ArrayList<>();
//        ArrayList<ArrayList<String>> allData = new ArrayList<>();
//        ArrayList<String> sheets = new ArrayList<>();
//        ArrayList<String> header1 = new ArrayList<>();
//        header1.add("Header1");
//        header1.add("Header2");
//        header1.add("Header3");
//        headers.add(header1);
//
////        ArrayList<String> header2 = new ArrayList<>();
////        header2.add("Header1");
////        header2.add("Header2");
////        header2.add("Header3");
////        headers.add(header2);
//
//
//        ArrayList<String> data1 = new ArrayList<>();
//        data1.add("value1");
//        data1.add("value2");
//        data1.add("value3");
//        data1.add("value4");
//        allData.add(data1);
//
////        ArrayList<String> data2 = new ArrayList<>();
////        data2.add("value1");
////        data2.add("value2");
////        data2.add("value3");
////        data2.add("value4");
////        data2.add("value5");
////        allData.add(data2);
//
//        sheets.add("sheet1");
////        sheets.add("sheet2");
//
//
//        createCustomisedExcel(sheets, headers, allData, "D:\\report\\newExcel");

        return "ok";
    }

    /**
     * Fill the report format and call a method for generating file
     *
     * @param map  it takes values of jrxmlFilePath,
     *             reportFormat,reportName and destinationPath from user
     * @param parameters it takes values for fill up jrxml template
     * @return destination path of downloaded file
     * @throws FileNotFoundException
     * @throws JRException
     */
    public String generateReport(Map map, Map parameters) throws IOException, JRException, GeneralSecurityException {
        File file = ResourceUtils.getFile("classpath:" + map.get("jrxmlFilePath"));
        JasperReport Report = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, parameters, new JREmptyDataSource());
        getFormattedFile(map, jasperPrint);


        return "report generated in path : " + map.get("destinationPath");
    }


    /**
     * Generate formatted file
     *
     * @param map   it takes values of jrxmlFilePath,
     *              reportFormat,reportName,password (it is not mandatory) and destinationPath from user
     * @param jasperPrint filled up jasper template
     * @throws JRException
     * @throws FileNotFoundException
     */

    public void getFormattedFile(Map<String, String> map, JasperPrint jasperPrint) throws JRException, IOException, GeneralSecurityException {
        if (map.get("reportFormat").equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, map.get("destinationPath") + "\\" + map.get("reportName") + ".html");
        } else if (map.get("reportFormat").equalsIgnoreCase("pdf")) {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            map.get("destinationPath") + "\\" + map.get("reportName") + ".pdf"
                    )
            ));

            if (map.containsKey("password")) {
                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                configuration.setEncrypted(true);
                configuration.set128BitKey(true);
                configuration.setUserPassword(map.get("password"));
                configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
                exporter.setConfiguration(configuration);
            }

            exporter.exportReport();

        } else if (map.get("reportFormat").equalsIgnoreCase("xlsx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            map.get("destinationPath") + "\\" + map.get("reportName") + ".xlsx"
                    )
            ));

            if (map.containsKey("password")) {
                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setPassword(map.get("password"));
                exporter.setConfiguration(configuration);
            }
            exporter.exportReport();


        } else if (map.get("reportFormat").equalsIgnoreCase("docx")) {

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            map.get("destinationPath") + "\\" + map.get("reportName") + ".docx"
                    )
            ));
            if (map.containsKey("password")) {
                SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
                configuration.setMetadataAuthor(map.get("password"));
                exporter.setConfiguration(configuration);

            }
            exporter.exportReport();

        } else if (map.get("reportFormat").equalsIgnoreCase("csv")) {

            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(
                            map.get("destinationPath") + "\\" + map.get("reportName") + ".csv"
                    )
            );

            exporter.exportReport();
        }
    }

    /**
     * generate customised excel file with n number of sheet
     * @param sheets name of sheets
     * @param headers list of headers for every sheet
     * @param allData list of data for every sheet
     * @param pathAndFileName destination path and file name
     */
    public void createCustomisedExcel(List<String> sheets, ArrayList<ArrayList<String>> headers, ArrayList<ArrayList<String>> allData, String pathAndFileName) {
        Workbook workbook = new XSSFWorkbook();
        for (int item = 0; item < sheets.size(); item++) {
            Sheet sheet = workbook.createSheet(sheets.get(item));
            loadDataInExcelFile(sheet, headers.get(item), allData.get(item));
        }
        try {
            workbook.write(new FileOutputStream(pathAndFileName+".xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataInExcelFile(Sheet sheet, ArrayList<String> header, ArrayList<String> data) {
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
