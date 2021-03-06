package com.jasperreportusingjava.jasperrepot.Controller;

import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import com.jasperreportusingjava.jasperrepot.service.BaseReportService;
import com.jasperreportusingjava.jasperrepot.service.EmployeeService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * @author Nazim Uddin Asif
 * @since 28/Mar/2021
 */
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private EmployeeService service;

    @Autowired
    private BaseReportService baseReportService;

    @GetMapping("/getEmployees")
    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    @PostMapping("/report")
    public void generateReport(@RequestBody Map map) throws IOException, JRException, GeneralSecurityException {
//        return service.getCustomisedDataForReport(map);
         baseReportService.generateReport();
    }
    @PostMapping("/report1")
    public ResponseEntity<byte[]> generateReport1(@RequestBody Map map) throws IOException, JRException, GeneralSecurityException {
//        return service.getCustomisedDataForReport(map);
        return  baseReportService.generateReport();
//        return ResponseEntity
//                .ok()
//                // Specify content type as PDF
//                .header("Content-Type", "application/pdf; charset=UTF-8")
//                // Tell browser to display PDF if it can
//                .header("Content-Disposition", "inline; filename=\"" + "report" + ".pdf\"")
//                .body(bytes);
    }
}
