### A COVID-19 modeling and forecast tool

A tool to analyse the number of daily COVID-19 infections and deaths, and to build a model that forecasts infections and deaths in the coming weeks, written in Java.

## COVID-19 Data files:
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
