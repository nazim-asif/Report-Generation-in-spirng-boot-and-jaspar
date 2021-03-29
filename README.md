# What is it?
Its fully user customized library for generating report using spring boot and jasper report. It supports **docx, csv, html, xlsx, pdf** format for generating report. 
First of all, you provide jrxml file format, destination file path, format you want to download, and name of report you want. 
Then you configure your jrxml file with your desire data then call **generateReport** function with pass the parameter using two maps 
one is containig this map key value pair (example is below json format) another is your customised key value for jrxml that’s it. It is cool right? 

_Json formate..._

```
{
    "jrxmlFilePath":"jrxml formates/employeeCustomTemplate.jrxml",
    "reportFormat":"html",
    "reportName":"employe_report",
    "destinationPath":"D:\\report"
    
}```


