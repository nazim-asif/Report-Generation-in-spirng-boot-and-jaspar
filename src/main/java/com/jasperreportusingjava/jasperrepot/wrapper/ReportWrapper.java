package com.jasperreportusingjava.jasperrepot.wrapper;

import lombok.*;

/**
 * @author Nazim Uddin Asif
 * @since 07/Apr/2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportWrapper {
     private String jrxmlFilePath;
     private String reportFormat;
     private String reportName;
     private String password;
     private String destinationPath;


}
