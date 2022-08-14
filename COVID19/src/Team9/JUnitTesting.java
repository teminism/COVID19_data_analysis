package Team9;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class JUnitTesting {
    @Test
    public void getLinearRegression() {
        //Test to ensure linear regression calculation is working
        ArrayList<Point> inputData = new ArrayList<>();
        ArrayList<Point> expectedOutput = new ArrayList<>();
        ArrayList<Point> actualOutput;

        inputData.add(new Point(749, 18628329));
        inputData.add(new Point(750, 18661296));
        inputData.add(new Point(751, 18695709));
        inputData.add(new Point(752, 18733105));
        inputData.add(new Point(753, 18777947));
        inputData.add(new Point(754, 18816692));
        inputData.add(new Point(755, 18852714));
        inputData.add(new Point(756, 18882943));
        inputData.add(new Point(757, 18910497));
        inputData.add(new Point(758, 18935313));
        inputData.add(new Point(759, 18966400));
        inputData.add(new Point(760, 19010668));
        inputData.add(new Point(761, 19054952));
        inputData.add(new Point(762, 19100647));
        inputData.add(new Point(763, 19143892));
        inputData.add(new Point(764, 19184875));
        inputData.add(new Point(765, 19223094));
        inputData.add(new Point(766, 19271183));
        inputData.add(new Point(767, 19335107));
        inputData.add(new Point(768, 19373884));

        expectedOutput.add(new Point(20, 19380360));
        expectedOutput.add(new Point(21, 19418684));
        expectedOutput.add(new Point(22, 19457008));
        expectedOutput.add(new Point(23, 19495331));
        expectedOutput.add(new Point(24, 19533655));
        expectedOutput.add(new Point(25, 19571979));
        expectedOutput.add(new Point(26, 19610302));
        expectedOutput.add(new Point(27, 19648626));
        expectedOutput.add(new Point(28, 19686950));
        expectedOutput.add(new Point(29, 19725273));
        expectedOutput.add(new Point(30, 19763597));
        expectedOutput.add(new Point(31, 19801921));
        expectedOutput.add(new Point(32, 19840244));
        expectedOutput.add(new Point(33, 19878568));
        expectedOutput.add(new Point(34, 19916892));
        expectedOutput.add(new Point(35, 19955215));
        expectedOutput.add(new Point(36, 19993539));
        expectedOutput.add(new Point(37, 20031863));
        expectedOutput.add(new Point(38, 20070186));
        expectedOutput.add(new Point(39, 20108510));

        actualOutput = LinearRegress.getForecastedData(inputData, 20);
        assertEquals(expectedOutput,actualOutput);
    }

    @Test
    public void getTextHeight() {
        WritePDF writePDF = new WritePDF();
        writePDF.font = PDType1Font.HELVETICA;
        float textHeight = writePDF.getTexHeight(12);

        assertEquals(textHeight, 13.872f,0.0f);
    }

    @Test
    public void getTextWidth() {
        WritePDF writePDF = new WritePDF();
        writePDF.font = PDType1Font.HELVETICA;
        float textHeight = 0;

        try{
            textHeight = writePDF.getTextWidth("Test String", 12);
        } catch (Exception e){
            System.out.println(e);
        }

        assertEquals(textHeight, 58.02f,0.0f);
    }
}