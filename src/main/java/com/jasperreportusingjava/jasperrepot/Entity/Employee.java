package com.jasperreportusingjava.jasperrepot.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nazim Uddin Asif
 * @since 22/Mar/2021
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "tbl_employee")
public class Employee {
    @Id
    private int id;
    private String name;
    private String designation;
    private double salary;
    private String doj;
}

