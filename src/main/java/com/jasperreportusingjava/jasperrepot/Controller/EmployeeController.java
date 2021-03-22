package com.jasperreportusingjava.jasperrepot.Controller;

import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import com.jasperreportusingjava.jasperrepot.service.EmployeeService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Nazim Uddin Asif
 * @since 22/Mar/2021
 */
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private EmployeeService service;

    @GetMapping("/getEmployees")
    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format)
            throws FileNotFoundException, JRException {
        return service.exportReport(format);
    }
}
