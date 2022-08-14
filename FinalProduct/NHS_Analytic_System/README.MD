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
