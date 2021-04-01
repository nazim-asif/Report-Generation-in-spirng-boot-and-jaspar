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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pojo.TableStructure;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
     *
     * @param map        it takes values of jrxmlFilePath,
     *                   reportFormat,reportName and destinationPath from user
     * @param parameters it takes values for fill up jrxml template
     * @return destination path of downloaded file
     * @throws FileNotFoundException
     * @throws JRException
     */
    public String generateReport(Map map, Map parameters) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile("classpath:" + map.get("jrxmlFilePath"));
        JasperReport Report = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, parameters, new JREmptyDataSource());
//        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, parameters, getDataSource());
        getFormattedFile(map, jasperPrint);


        return "report generated in path : " + map.get("destinationPath");
    }


    /**
     * Generate formatted file
     * @param map it takes values of jrxmlFilePath,
     *        reportFormat,reportName,password (it is not mandatory) and destinationPath from user
     * @param jasperPrint filled up jasper template
     * @throws JRException
     * @throws FileNotFoundException
     */

     public void getFormattedFile(Map<String, String> map, JasperPrint jasperPrint) throws JRException, FileNotFoundException {
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

            if(map.containsKey("password"))
            {
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
            if(map.containsKey("docx"))
            {
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



}
