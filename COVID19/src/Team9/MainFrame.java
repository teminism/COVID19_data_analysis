package Team9;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static Team9.Config.*;


public class MainFrame {

    private final ArrayList<CovidData> covidData;


    public MainFrame() {
        JPanel main = new JPanel(new BorderLayout());

        covidData = new ArrayList<>();

        //Read metrics from file
        try {
            Scanner scanner = new Scanner(new File(COVID_DATA_PATH));
            scanner.nextLine(); //Headers

            while (scanner.hasNextLine()) { //Each line
                String[] row = scanner.nextLine().split(",");   //Parse csv to pass into CovidData instance

                String dataType = "Cases";
                if (row[4].contains("Deaths")){
                    dataType = "Deaths";
                }

                covidData.add(new CovidData(row[0],row[1],row[2],row[3].replace("/NL", "\n\n"),row[4], dataType));
                covidData.get(covidData.size() - 1).downloadData(); //Download data to csv file
            }
        } catch (Exception e){
            System.out.println(e);
        }

        //Creating UI
        JPanel topPanel = new JPanel();

        topPanel.setPreferredSize(new Dimension(1400, 600));
        topPanel.setLayout(null);

        //Graph labels
        JLabel lblDailyDeaths = new JLabel(covidData.get(0).getDataTitle());
        JLabel lblDailyCases = new JLabel(covidData.get(1).getDataTitle());
        JLabel lblCumDeaths = new JLabel(covidData.get(2).getDataTitle());
        JLabel lblCumCases = new JLabel(covidData.get(3).getDataTitle());

        lblDailyDeaths.setBounds(150, 20, 1000, 20);
        lblDailyCases.setBounds(800, 20, 1000, 20);
        lblCumDeaths.setBounds(150, 310, 1000, 20);
        lblCumCases.setBounds(800, 310, 1000, 20);

        topPanel.add(lblDailyDeaths);
        topPanel.add(lblDailyCases);
        topPanel.add(lblCumDeaths);
        topPanel.add(lblCumCases);

        //Graphs
        GenerateGraph graph1 = new GenerateGraph(covidData.get(0)); //Daily deaths
        GenerateGraph graph2 = new GenerateGraph(covidData.get(1)); //Daily cases
        GenerateGraph graph3 = new GenerateGraph(covidData.get(2)); //Cumulative deaths
        GenerateGraph graph4 = new GenerateGraph(covidData.get(3)); //Cumulative cases

        graph1.setBounds(50, 20, 600, 300);
        graph2.setBounds(700, 20, 600, 300);
        graph3.setBounds(50, 310, 600, 300);
        graph4.setBounds(700, 310, 600, 300);

        topPanel.add(graph1);
        topPanel.add(graph2);
        topPanel.add(graph3);
        topPanel.add(graph4);



        //Bottom topPanel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(1400, 62));
        bottomPanel.setLayout(null);

        //Button
        JButton btnReport = new JButton("Open Report");

        //Combobox
        JComboBox<String> cboData = new JComboBox<>();

        //Button action listeners
        btnReport.addActionListener(e -> {
            new ReportFrame(Main.getSelectedData(cboData.getSelectedItem().toString(), covidData), covidData);
        });

        //Populate combobox
        for (CovidData data: covidData){
            cboData.addItem(data.getDataTitle());
        }

        //Adding components to bottomPanel
        cboData.setBounds(220, 21, 450, 20);
        btnReport.setBounds(780, 16, 200, 30);

        bottomPanel.add(cboData);
        bottomPanel.add(btnReport);



        //Final frame assembly
        main.add(topPanel, BorderLayout.NORTH);
        main.add(bottomPanel, BorderLayout.SOUTH);

        JFrame frame = new JFrame("NHS Analytic System for COVID Data");
        frame.setSize(new Dimension(1400, 700));
        frame.setResizable(false);

        frame.add(main);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
