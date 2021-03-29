package com.jasperreportusingjava.jasperrepot.Controller;

import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import com.jasperreportusingjava.jasperrepot.repository.EmployeeRepository;
import com.jasperreportusingjava.jasperrepot.service.EmployeeService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
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

    @GetMapping("/getEmployees")
    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    @PostMapping("/report")
    public String generateReport(@RequestBody Map map) throws FileNotFoundException, JRException {
        return service.getCustomisedDataForReport(map);
    }
}
