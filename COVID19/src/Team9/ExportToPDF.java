package Team9;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import static Team9.Config.*;

public class ExportToPDF {

    private final WritePDF pdf = new WritePDF();
    private final ReportFrame reportFrame;

    public ExportToPDF(String filepath, ReportFrame reportFrame) throws IOException {
        this.reportFrame = reportFrame;

        pdf.sideMargin = 30;
        pdf.font = PDType1Font.HELVETICA;

        //Title page
        PDPage titlePage = new PDPage();
        addTitlePage(titlePage);
        pdf.doc.addPage(titlePage);

        //Page 1
        PDPage page1 = new PDPage();
        addPage1(page1);
        pdf.doc.addPage(page1);

        //Graph pages
        addGraphPages();


        if (!(filepath.endsWith(".pdf") || filepath.endsWith(".PDF"))) {
            filepath += ".pdf";
        }

        //Path where the PDF file will be store
        pdf.doc.save(filepath);
        System.out.println("PDF CREATED");

        //Close the document
        pdf.doc.close();
    }

    //Add pages
    private void addTitlePage(PDPage page) throws IOException {
        addHeader(page);

        //Center title
        pdf.addTextAligned(page, "NHS Covid Data Report", 30, 200, CENTER, Color.BLACK);
        pdf.drawLineAligned(page, 0, 2f, 250, 100, CENTER, NHS_COLOR);
        pdf.addTextAligned(page, reportFrame.data.getDataTitle(), 14, 260, CENTER, Color.BLACK);

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        pdf.addTextAligned(page, "Report generated on " + todayDate, 10, 680, CENTER, Color.BLACK);
    }

    private void addPage1(PDPage page) throws IOException {
        addHeader(page);

        //Page title
        pdf.addTextAligned(page, reportFrame.data.getDataTitle(), 14, 75, LEFT, Color.BLACK);
        pdf.drawLineAligned(page, pdf.getTextWidth(reportFrame.data.getDataTitle(), 14) + 30, 1f, 100, 0, LEFT, NHS_COLOR);

        //Metrics information
        pdf.addTextAligned(page, "Metrics Information", 12, 130, LEFT, Color.BLACK);
        pdf.drawLineAligned(page, pdf.getTextWidth("Metrics Information", 12) + 20, 1f, 150, 0, LEFT, NHS_COLOR);

        pdf.addParagraph(page, reportFrame.txtDescription.getText(), 10, 160, LEFT, Color.BLACK);

        pdf.addTextAligned(page, "Find out more information on this metric:", 10, 260, LEFT, Color.BLACK);
        pdf.addTextAligned(page, reportFrame.data.getMetricLink(), 10, 270, LEFT, Color.BLUE);

        pdf.addTextAligned(page, "Visit GOV.UK Coronavirus statistics website:", 10, 300, LEFT, Color.BLACK);
        String govLink = "https://coronavirus.data.gov.uk/details/" + reportFrame.data.getDataType().toLowerCase();
        pdf.addTextAligned(page, govLink, 10, 310, LEFT, Color.BLUE);

        //Basic graph
        pdf.addGraphImage(page, reportFrame, 610, 300, pdf.sideMargin, 400, CENTER, false, 0);
    }

    private void addGraphPages() throws IOException {
        boolean showNext = true;
        boolean showCustomForecastPage = true;

        int numDaysToForecast = Integer.parseInt(reportFrame.txtDaysToForecast.getText());

        //Shows the next graph page if the graph above does not stop at 0
        //Includes other criteria
        if (numDaysToForecast < 30){
            showNext = addCustomGraph(numDaysToForecast);
            showCustomForecastPage = false;
        }
        if (showNext){
            showNext = addMonthGraph();
        }
        if (showNext && showCustomForecastPage && numDaysToForecast != 30 && (numDaysToForecast < 365)){
            showNext = addCustomGraph(numDaysToForecast);
        }
        if (showNext){
            addYearGraph();
        }
    }
    private boolean addMonthGraph() throws IOException {
        String title = "Forecasting 30 days (month)";
        return addGraphPage(title, 30);
    }
    private boolean addCustomGraph(int numDaysToForecast) throws IOException {
        String title = "Forecasting " + numDaysToForecast + " days";
        return addGraphPage(title, numDaysToForecast);
    }
    private boolean addYearGraph() throws IOException {
        String title = "Forecasting " + 365 + " days (year)";
        return addGraphPage(title, 365);
    }
    private boolean addGraphPage(String title, int daysToForecast) throws IOException {
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));

        addHeader(page);

        //Page title
        pdf.addTextAligned(page, title, 14, 75, LEFT, Color.BLACK);
        pdf.drawLineAligned(page, pdf.getTextWidth(title, 14) + 30, 1f, 100, 0, LEFT, NHS_COLOR);

        //Add forecasted graph
        boolean reached0 = pdf.addGraphImageFinal(page, reportFrame, 840, 400, pdf.sideMargin, 120, CENTER, true, daysToForecast);

        pdf.doc.addPage(page);

        return reached0;
    }


    private void addHeader(PDPage page) throws IOException {
        pdf.addRectangle(page, NHS_COLOR, 0, 0, 1000, 60);
        pdf.addTextAligned(page, "National Health Service", 10, 22, CENTER, Color.WHITE);
    }
}