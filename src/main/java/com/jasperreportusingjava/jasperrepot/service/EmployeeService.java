package com.jasperreportusingjava.jasperrepot.service;


import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nazim Uddin Asif
 * @since 22/Mar/2021
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;


    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "D:\\report";
        List<Employee> employees = repository.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:Employees.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Nazim Uddin Asif");
        JasperReport Report = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        JasperPrint jasperPrint = JasperFillManager.fillReport(Report, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
        }

        return "report generated in path : " + path;
    }
}
