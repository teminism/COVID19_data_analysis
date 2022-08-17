# A COVID-19 modeling and forecast tool

A tool to analyse the number of daily COVID-19 infections and deaths, and to build a model that forecasts infections and deaths in the coming weeks, written in Java.

**Main Window**  
When starting the app, the following window is shown. It includes 4 graphs for quick analysis and a dropdown menu with a button to allow the user to select a specific metric to view the report. Clicking on "Open Report" will open a report window which can be seen below.  
&nbsp;  
![Main Window](/FinalProduct/img/ProductDemonstration/MainDropDown.png)  
&nbsp;  
&nbsp;  
**Report Window**  
The display of this window is based on the selected metric from the starting window. It also includes a dropdown that can be used to select a different metric, once clicked the report window will update displaying the graph and text for the new metric, the content of the dropdown is the same as the dropdown menu from the main frame. A clickable hyperlink for the current metric documentation is included in this window.  
&nbsp;  
![Report Window](/FinalProduct/img/ProductDemonstration/ReportFrame.png)  
&nbsp;  
Report window after clicking a different metric:  
![Report Window2](/FinalProduct/img/ProductDemonstration/ReportFrame2.png)  
&nbsp;  
&nbsp;  
**Report Window - forecasting**  
To enable the forecasting on the graph the "Forecast" checkbox needs to be checked, the length of the forecast is derived from the "Days to forecast" text field; it has a default value of 30, disabled if "Forecast" is not checked, only allows number inputs between 0-365 and updates the report window automatically when typing. 2 Extra graph legend entries and the forecast information statistics are shown, they are hidden when the forecast checkbox is not checked.   
&nbsp;  
Two examples of forecasting:  
![Report Window Forecast](/FinalProduct/img/ProductDemonstration/ReportFrameForecast1.png)  
![Report Window Forecast](/FinalProduct/img/ProductDemonstration/ReportFrameForecast2.png)  
&nbsp;  
Messages that are displayed when an input above 365 is entered and a negative number input respectively. (Can only reach negative message by pasting in a negative value)  
![Input Too Large Message](/FinalProduct/img/ProductDemonstration/InputTooLargeMessage.png)  ![Negative Input Message](/FinalProduct/img/ProductDemonstration/NegativeInputMessage.png)  
&nbsp;  
&nbsp;  
**Report Window - negative forecasting**  
If a forecast happens to go below 0 it will stop any further forecasting as it's not possible to have negative deaths/cases, this can be seen below with the days to forecast being 365 and it only forecasting 16 days. If this is the case the exported PDF report will not contain the month and yearly forecast graphs as they would be exactly the same as the below.   
&nbsp;  
![Report Window Forecast < 0](/FinalProduct/img/ProductDemonstration/ReportFrameForecastBelow0.png) 
&nbsp;  
&nbsp;  
**File Chooser**  
This file chooser appears when the "Export to PDF" button is clicked, the user can then browse to a location to save the PDF file and provide a name for the file. A message is displayed if the file has been saved successfully and also if an error has occurred when attempting to create and save the PDF file.  
&nbsp;  
![File Chooser](/FinalProduct/img/ProductDemonstration/FileChooser.png)  
&nbsp;  
PDF not saved and PDF saved messages respectively:  
![PDF Not Saved Message](/FinalProduct/img/ProductDemonstration/PDFNotSavedMessage.png)
![PDFSavedMessage](/FinalProduct/img/ProductDemonstration/PDFSavedMessage.png)  
&nbsp;  
&nbsp;  
**Exporting PDF**  
The following PDF report is generated when the "Export to PDF" is clicked on the below report window. Note, 3 graphs are attempted to be added to the PDF report (30 days, custom forecast days and 365 days), the graphs are shown in ascending order and if the custom forecast is 30 or 365 then that graph is shown respectively instead of the custom forecast.
&nbsp; 
&nbsp;  
![Report Window for PDF Export Example](/FinalProduct/img/ProductDemonstration/PDFReportExportExample.png)  
&nbsp;  
PDF report created from the window above:  
Page 1  
![Report1](/FinalProduct/img/ProductDemonstration/Report1.jpg)  
Page 2  
![Report2](/FinalProduct/img/ProductDemonstration/Report2.jpg)  
Page 3  
![Report3](/FinalProduct/img/ProductDemonstration/Report3.jpg)  
Page 4  
![Report4](/FinalProduct/img/ProductDemonstration/Report4.jpg)  
Page 5  
![Report5](/FinalProduct/img/ProductDemonstration/Report5.jpg)  

## Incomplete sections

One aspect we did not get round to is chaning the style of the windows to match a similar theme to NHS, preferably a similar style to the generated PDF report.

If the user does cancels the Export to PDF action it just closes the file chooser, we would prefer it to have some feedback saying the file has not been saved.

### COVID-19 Data files:
The relevant files containing the number of daily infections and deaths can be downloaded from the following government webpages:  https://coronavirus.data.gov.uk/cases and https://coronavirus.data.gov.uk/deaths. You can download the card data in CSV format. The data is organised by different criteria. Discuss in your team which data you like to select and analyse.

### Prerequisites
- Have at least Java Runtime Environment 8 installed
- Have the files laid out as such:
```
    - NHS_Analytic_System.jar
    - CovidFiles (Folder)
        - CovidData.csv
        - CovidDataFiles (Folder)
```
There may already be some CSV files inside CovidDataFiles, these will be overwritten each time the program is run.
- PDFBox 2.0.25 added to Class Path - https://pdfbox.apache.org/download.html
- Main.java contains the Main class  
&nbsp;  
&nbsp;  
### Folders and Files
- CovidData.csv holds information about each metric (Data Permalink, Data (more commonly known as "title"), Metrics documentation link, Quick description, Metrics Name). This is where the inital information about where the data is downloaded from is stored and read from.  
- CovidDataFiles is a folder that will store the downloaded CSV files.  

There are a few requirements when adding new metrics to CovidData.csv, these can be found at the bottom of "ProductImplementationReport.md".  
&nbsp;  
&nbsp;  
### Running the jar executable
The NHS_Analytic_System.jar can be run either by double clicking on the file or typing by "java -jar NHS_Analytic_System.jar" from the command line.

https://cseegit.essex.ac.uk/21-22-ce201-col/21-22_CE201-col_team36/-/blob/0d2d08e7541614630121ad9e97c07391422b7226/FinalProduct/NHS_Analytic_System/NHS_Analytic_System.jar
