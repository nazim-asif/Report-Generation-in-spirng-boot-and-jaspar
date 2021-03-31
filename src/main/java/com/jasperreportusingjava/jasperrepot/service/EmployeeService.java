package com.jasperreportusingjava.jasperrepot.service;


import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    /**
     * This method is customizing the data for jrxml file.
     * @param map it takes the value of jrxmlFilePath,
     *            reportFormat,reportName and destinationPath from user
     * @return It returns destination path of downloaded file
     * @throws FileNotFoundException
     * @throws JRException
     */

    public String getCustomisedDataForReport(Map map) throws FileNotFoundException, JRException {
        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("name", "Nazim Uddin Asif");
//        parameters.put("reportTitle", "Employee Info");
//        parameters.put("designation", "Manager");
//        parameters.put("reportDescription", "Employee Information Report");
//
//        List<Employee> employees = repository.findAll();
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
//        parameters.put("tblData", dataSource);

        return generateReport(map, parameters);
    }

    /**
     * Fill the report format and call a method for generating file
     * @param map it takes values of jrxmlFilePath,
     *            reportFormat,reportName and destinationPath from user
     * @param parameters it takes values for fill up jrxml template
     * @return destination path of downloaded file
     * @throws FileNotFoundException
     * @throws JRException
     */
    public String generateReport(Map map, Map parameters) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile("classpath:"+map.get("jrxmlFilePath"));
        JasperReport Report = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, parameters, new JREmptyDataSource());
        getFormattedFile((String) map.get("reportFormat"), jasperPrint, (String) map.get("destinationPath"), (String) map.get("reportName"));


        return "report generated in path : " + map.get("destinationPath");
    }

    /**
     * Generate file
     * @param reportFormat Name of format
     * @param jasperPrint fill up template
     * @param path destination path
     * @param reportName report name
     * @throws JRException
     * @throws FileNotFoundException
     */

    public void getFormattedFile(String reportFormat, JasperPrint jasperPrint, String path, String reportName) throws JRException, FileNotFoundException {
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\"+reportName+".html");
        }

        else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\"+reportName+".pdf");

        }

        else if (reportFormat.equalsIgnoreCase("xlsx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            path + "\\"+reportName+".xlsx"
                    )
            ));
            exporter.exportReport();
        }
        else if (reportFormat.equalsIgnoreCase("xls")) {
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true);
            configuration.setWhitePageBackground(false);
            configuration.setFontSizeFixEnabled(false);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);


            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setConfiguration(configuration);
            exporter.setExporterOutput( new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            path + "\\"+reportName+".xls"
                    )
            ));
            exporter.exportReport();
        }

        else if (reportFormat.equalsIgnoreCase("docx")) {

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput( new SimpleOutputStreamExporterOutput(
                    new FileOutputStream(
                            path + "\\"+reportName+".docx"
                    )
            ));
            exporter.exportReport();
        }

        else if (reportFormat.equalsIgnoreCase("csv")) {

            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput( new SimpleWriterExporterOutput(
                            path + "\\"+reportName+".csv"
                    )
            );
            exporter.exportReport();
        }
    }
}
