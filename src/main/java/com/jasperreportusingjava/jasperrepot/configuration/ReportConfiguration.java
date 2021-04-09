package com.jasperreportusingjava.jasperrepot.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nazim Uddin Asif
 * @since 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportConfiguration {
    private String templateName;
    private String reportName;
}
