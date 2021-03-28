package com.jasperreportusingjava.jasperrepot.service;


import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nazim Uddin Asif
 * @since 28/Mar/2021
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;

    public Map dataCustomisation() throws FileNotFoundException, JRException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "Nazim Uddin Asif");
        parameters.put("reportTitle", "Employee Info");
        parameters.put("designation", "Manager");
        parameters.put("reportDescription", "Employee Information Report");

        List<Employee> employees = repository.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        parameters.put("tblData", dataSource);

        return parameters;
    }

    public String generateReport(Map map) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile("classpath:"+map.get("jrxmlFilePath"));
        JasperReport Report = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, dataCustomisation(), new JREmptyDataSource());
        formattedFile((String) map.get("reportFormat"), jasperPrint, (String) map.get("path"));


        return "report generated in path : " + map.get("path");
    }

    public void formattedFile(String reportFormat, JasperPrint jasperPrint, String path) throws JRException, FileNotFoundException {
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees_custom.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees_custom.pdf");

        }
        if (reportFormat.equalsIgnoreCase("excel")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            path + "\\employees_custom.xlsx"
                    )
            ));
            exporter.exportReport();
        }

        if (reportFormat.equalsIgnoreCase("docx")) {

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput( new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            path + "\\employees_custom.docx"
                    )
            ));
            exporter.exportReport();
        }
        if (reportFormat.equalsIgnoreCase("csv")) {

            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput( new SimpleWriterExporterOutput(
                            path + "\\employees_custom.csv"
                    )
            );
            exporter.exportReport();
        }
    }
}
