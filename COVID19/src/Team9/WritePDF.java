package Team9;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static Team9.Config.*;

public class WritePDF {

    protected int sideMargin;
    protected PDFont font;

    protected final PDDocument doc = new PDDocument();


    //Add text
    protected void addTextAligned(PDPage page, String text, float fontSize, float y, int align, Color color) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();

        float textWidth = getTextWidth(text, fontSize);
        float textHeight = getTexHeight(fontSize);

        float x = (mediaBox.getWidth() - textWidth) / 2;
        y = mediaBox.getHeight() - textHeight - y;

        if (align == LEFT) {
            x = sideMargin;
        }

        addTextFinal(page, text, fontSize, x, y, color);
    }

    protected void addTextAbsolute(PDPage page, String text, float fontSize, float x, float y, Color color) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();
        y = mediaBox.getHeight() - y;

        addTextFinal(page, text, fontSize, x, y, color);
    }

    private void addTextFinal(PDPage page, String text, float fontSize, float x, float y, Color color) throws IOException {
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

        contents.beginText();
        contents.setFont(font, fontSize);
        contents.setNonStrokingColor(color);
        contents.newLineAtOffset(x, y);
        contents.showText(text);
        contents.endText();
        contents.close();
    }


    protected void addParagraph(PDPage page, String text, float fontSize, float y, int align, Color color) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();
        boolean finished = false;

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(text.split("\n")));


        while (!finished) {
            finished = true;

            int nextSpacePos = 0;
            int lineNo = -1;

            for (String line : lines) { //For each line of text
                lineNo++;

                //If line is smaller than page space - goto next line
                if (getTextWidth(line, fontSize) < mediaBox.getWidth() - sideMargin * 2) {
                    continue;
                }


                //Crawl from left of line to right testing length at each space position
                //until it either reaches the last space or a split needs to be made
                while (nextSpacePos < line.lastIndexOf(" ")) {   //While next space pos is smaller than the last space position in line

                    nextSpacePos = line.indexOf(" ", nextSpacePos) + 1; //Get next space position

                    float textWidth = getTextWidth(line.substring(0, nextSpacePos), fontSize);  //Get text width of text up to next space position

                    if (textWidth > mediaBox.getWidth() - sideMargin * 2) { //If text up to next space position is larger than page space
                        nextSpacePos = line.lastIndexOf(" ", nextSpacePos - 2); //Get previous space position (the split position)
                        finished = false;
                        break;
                    }
                }

                if (!finished) {
                    break;
                }
            }

            if (!finished) { //Split at nextLinePos and add to lines
                String line = lines.get(lineNo);

                String oldLine = line.substring(0, nextSpacePos);
                String newLine = line.substring(nextSpacePos + 1);

                lines.set(lineNo, oldLine);
                lines.add(lineNo + 1, newLine);
            }
        }

        //Get height of each line using the first line
        float textHeight = getTexHeight(fontSize);

        for (String line : lines) {   //Call addText for each line and increase height
            addTextAligned(page, line, fontSize, y, align, color);
            y += textHeight;
        }
    }

    protected void drawLineAligned(PDPage page, float width, float thickness, float y, float edgePadding, int align, Color color) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();

        y = mediaBox.getHeight() - y;
        float x1 = edgePadding;
        float x2 = mediaBox.getWidth() - edgePadding;

        if (align == LEFT) {
            x1 = sideMargin;
            x2 = x1 + width;
        }

        drawLineFinal(page, thickness, x1, x2, y, color);
    }

    private void drawLineFinal(PDPage page, float thickness, float x1, float x2, float y, Color color) throws IOException {
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

        contents.moveTo(x1, y);
        contents.lineTo(x2, y);
        contents.setStrokingColor(color);
        contents.setLineWidth(thickness);
        contents.stroke();
        contents.close();
    }


    protected void addGraphImage(PDPage page, ReportFrame frame, int width, int height, float edgePadding, float y, int align, boolean forecast, int daysToForecast) throws IOException {
        addGraphImageFinal(page, frame, width, height, edgePadding, y, align, forecast, daysToForecast);
    }
    protected boolean addGraphImageFinal(PDPage page, ReportFrame frame, int width, int height, float edgePadding, float y, int align, boolean forecast, int daysToForecast) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();

        GenerateGraph graph;
        Font labelFont = new Font("TimesRoman", Font.PLAIN, 9);

        if (forecast) {
            graph = new GenerateGraph(frame.data, true, daysToForecast, labelFont);
        } else {
            graph = new GenerateGraph(frame.data, labelFont);
        }

        graph.setSize(new Dimension(width, height));

        //Paint JPanel to get the graphic
        BufferedImage graphImage = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = graphImage.createGraphics();
        graph.paint(g);
        g.dispose();

        //Read image into byteArray
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(graphImage, "png", byteStream);
        byte[] bytes = byteStream.toByteArray();

        PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, bytes, "");


        //Drawing the image in the PDF document
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

        float graphTop = y; //Because graph title is added via text align
        float x = (mediaBox.getWidth() - width) / 2 + 5;
        y = mediaBox.getHeight() - height - y;

        if (align == LEFT) {
            x = edgePadding;
        }


        //Add graph title
        addTextAligned(page, frame.data.getDataTitle(), 12, graphTop, CENTER, Color.BLACK);

        //Add graph
        contents.drawImage(pdImage, x, y);

        //Add graph legend
        x += 40;
        y = mediaBox.getHeight() - y - 30;

        addRectangle(page, DEFAULT_COLOR, x, y += 20, 10, 10);
        addTextAbsolute(page, "Data", 10, x + 15, y + 9.25f, Color.BLACK);

        if (forecast) {
            addRectangle(page, FORECAST_COLOR, x, y += 20, 10, 10);
            addTextAbsolute(page, "Forecast Data", 10, x + 15, y + 9.25f, Color.BLACK);

            addRectangle(page, MODEL_COLOR, x, y += 20, 10, 10);
            addTextAbsolute(page, "Model Data", 10, x + 15, y + 9.25f, Color.BLACK);


            //Forecast info
            String[] forecastLines = graph.showAndReturnForecastDetails(daysToForecast);
            addTextAbsolute(page, "Forecast:", 11, 350, 520, Color.BLACK);
            addTextAbsolute(page, forecastLines[0], 10, 350, 535, Color.BLACK);
            addTextAbsolute(page, forecastLines[1], 10, 350, 550, Color.BLACK);
            contents.close();
            return !forecastLines[1].startsWith("• 0");
        }

        contents.close();
        return true;
    }


    protected void addRectangle(PDPage page, Color color, float x, float y, float width, float height) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();

        y = mediaBox.getHeight() - height - y;

        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

        contents.setNonStrokingColor(color);
        contents.addRect(x, y, width, height);  //x,y lower left
        contents.fill();
        contents.setNonStrokingColor(Color.BLACK);  //Reset color otherwise all subsequent adds will be in previous colour

        contents.close();
    }


    //Helper methods
    protected float getTextWidth(String text, float fontSize) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
    }

    protected float getTexHeight(float fontSize) {
        return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
    }
}
