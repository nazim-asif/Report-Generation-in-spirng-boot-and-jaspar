package com.jasperreportusingjava.jasperrepot.repository;

import com.jasperreportusingjava.jasperrepot.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nazim Uddin Asif
 * @since 22/Mar/2021
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
