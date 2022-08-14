package Team9;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

import static Team9.Config.*;


public class ReportFrame {

    protected CovidData data;
    private final ArrayList<CovidData> covidData;
    private JPanel graphPanel;
    private GenerateGraph graph;
    private JLabel lblMetricsLink;
    protected JTextArea txtDescription;
    private JCheckBox ckbForecast;
    protected JTextField txtDaysToForecast;
    private JLabel lblForecastInfo;
    private JTextArea txtForecastInfo;

    private JLabel boxForecastData;
    private JLabel boxModel;
    private JLabel lblForecastData;
    private JLabel lblModel;

    private final JFrame frame;


    public ReportFrame(CovidData dataIn, ArrayList<CovidData> covidData) {
        this.data = dataIn;
        this.covidData = covidData;

        frame = new JFrame();
        createReport();
    }

    private void createReport() {
        JPanel main = new JPanel(new BorderLayout());

        //Graph
        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(1400, 600));

        graph = new GenerateGraph(data, false, 30);

        graphPanel.add(graph);


        //Extra
        JPanel bottomPanel = new JPanel(null);
        bottomPanel.setPreferredSize(new Dimension(1400, 180));

        //Data comboBox
        JLabel lblData = new JLabel("Data:");
        JComboBox<String> cboData;
        cboData = new JComboBox<>();

        for (CovidData e : covidData) {
            cboData.addItem(e.getDataTitle());
        }

        cboData.setSelectedItem(data.getDataTitle());

        lblData.setBounds(15, 0, 100, 20);
        cboData.setBounds(50, 0, 450, 20);

        cboData.addActionListener(e -> {
            int forecastLength = Integer.parseInt(txtDaysToForecast.getText());
            updateGraph(Objects.requireNonNull(cboData.getSelectedItem()).toString(), ckbForecast.isSelected(), forecastLength);
            updateText();
        });


        //Forecast checkBox
        JLabel lblForecast = new JLabel("Forecast:");
        ckbForecast = new JCheckBox();

        ckbForecast.addActionListener(e -> {
            txtDaysToForecast.setEditable(ckbForecast.isSelected());
            int forecastLength = Integer.parseInt(txtDaysToForecast.getText());
            updateGraph(Objects.requireNonNull(cboData.getSelectedItem()).toString(), ckbForecast.isSelected(), forecastLength);
            showAndReturnForecastDetails();
        });

        lblForecast.setBounds(15, 30, 100, 20);
        ckbForecast.setBounds(70, 30, 20, 20);


        //Days to forecast
        JLabel lblDaysToForecast = new JLabel("Days to forecast:");
        txtDaysToForecast = new JTextField();
        txtDaysToForecast.setEditable(false);
        txtDaysToForecast.setText("30");

        txtDaysToForecast.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {  //Ignores key input unless it is a number or backspace
                char c = e.getKeyChar();

                if ((c < '0' || c > '9') && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }

                if (Objects.equals(txtDaysToForecast.getText(), "0")) {
                    txtDaysToForecast.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int forecastLength = 0;

                try {
                    forecastLength = Integer.parseInt(txtDaysToForecast.getText());
                } catch (Exception ex) {
                    txtDaysToForecast.setText("0");
                }

                if (forecastLength > 365) {
                    JOptionPane.showMessageDialog(null, "Input is too large.\nMaximum forecast length is 365 days.", "Input too large", JOptionPane.ERROR_MESSAGE);
                    txtDaysToForecast.setText("365");
                } else {
                    updateGraph(cboData.getSelectedItem().toString(), ckbForecast.isSelected(), forecastLength);
                }
            }
        });

        lblDaysToForecast.setBounds(110, 30, 100, 20);
        txtDaysToForecast.setBounds(210, 30, 50, 20);


        //Export to PDF button
        JButton btnExport = new JButton("Export to PDF");
        btnExport.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
            fc.setFileFilter(filter);

            LookAndFeel originalStyle = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    new ExportToPDF(fc.getSelectedFile().toString(), this);
                }
            } catch (Exception ex) {}
            finally {   //Reset look and feel as it will change ReportFrame look and feel if reopened
                try { UIManager.setLookAndFeel(originalStyle);} catch (Exception ex) {}
            }
        });


        btnExport.setBounds(290, 30, 120, 20);


        //Metrics description textArea
        JLabel lblDescription = new JLabel("Metrics Description:");
        txtDescription = textAreaSetup();

        lblDescription.setBounds(550, 0, 800, 20);
        txtDescription.setBounds(550, 20, 800, 80);


        //Metrics URL link label
        JLabel lblMoreInfo = new JLabel("Find out more information on this metric:");

        lblMetricsLink = new JLabel(); //HTML to underline text
        lblMetricsLink.setForeground(Color.BLUE);
        lblMetricsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblMetricsLink.addMouseListener(new MouseAdapter() {    //Open hyperlink
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(data.getMetricLink()));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });


        lblMoreInfo.setBounds(15, 130, 700, 20);


        //Add graph legend
        lblForecastInfo = new JLabel("Forecast:");
        addGraphLegend(bottomPanel);

        lblForecastInfo.setBounds(550, 120, 400, 20);
        txtForecastInfo = textAreaSetup();
        txtForecastInfo.setBounds(550, 140, 600, 50);

        showAndReturnForecastDetails();


        //Adding components to bottomPanel
        bottomPanel.add(lblData);
        bottomPanel.add(cboData);
        bottomPanel.add(lblForecast);
        bottomPanel.add(ckbForecast);
        bottomPanel.add(lblDescription);
        bottomPanel.add(txtDescription);
        bottomPanel.add(lblMoreInfo);
        bottomPanel.add(lblMetricsLink);
        bottomPanel.add(lblDaysToForecast);
        bottomPanel.add(txtDaysToForecast);
        bottomPanel.add(btnExport);
        bottomPanel.add(lblForecastInfo);
        bottomPanel.add(txtForecastInfo);


        //Final frame construction
        main.add(graphPanel, BorderLayout.NORTH);
        main.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(new Dimension(1400, 820));
        frame.setResizable(false);

        frame.add(main);

        updateText();   //Fill in text

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void updateText() {
        frame.setTitle(data.getDataTitle() + " report");

        txtDescription.setText(data.getDescription() + "\n\n" + PEAK_AND_DATE_INFO.get(data.getDataType()));

        String text = "<HTML><U>" + data.getMetricLink() + "</U></HTML>";   //HTML to underline text
        lblMetricsLink.setText(text);

        lblMetricsLink.setBounds(15, 150, text.length() * 5 - 20, 20);
    }

    private void updateGraph(String selectedItem, boolean forecasted, int forecastLength) {
        graph = new GenerateGraph(Main.getSelectedData(selectedItem, covidData), forecasted, forecastLength);    //ADD NUMBER
        data = Main.getSelectedData(selectedItem, covidData);

        graphPanel.removeAll();
        graphPanel.add(graph);

        if (forecasted) {
            String[] lines = showAndReturnForecastDetails();
            txtForecastInfo.setText(String.join("\n", lines));
        }

        graphPanel.revalidate();
        graphPanel.repaint();
    }

    private void addGraphLegend(JPanel bottomPanel) {
        //Original data
        //Graph legend
        JLabel boxActualData = new JLabel();
        boxActualData.setBounds(15, 65, 10, 10);
        boxActualData.setBackground(DEFAULT_COLOR);
        boxActualData.setOpaque(true);

        JLabel lblActualData = new JLabel("Data");
        lblActualData.setBounds(35, 60, 200, 20);

        //Forecast data
        boxForecastData = new JLabel();
        boxForecastData.setBounds(15, 85, 10, 10);
        boxForecastData.setBackground(FORECAST_COLOR);
        boxForecastData.setOpaque(true);

        lblForecastData = new JLabel("Forecast data");
        lblForecastData.setBounds(35, 80, 200, 20);

        //Model data
        boxModel = new JLabel();
        boxModel.setBounds(15, 105, 10, 10);
        boxModel.setBackground(MODEL_COLOR);
        boxModel.setOpaque(true);

        lblModel = new JLabel("Model data");
        lblModel.setBounds(35, 100, 200, 20);

        //Add legend to panel
        bottomPanel.add(boxActualData);
        bottomPanel.add(lblActualData);
        bottomPanel.add(boxForecastData);
        bottomPanel.add(lblForecastData);
        bottomPanel.add(boxModel);
        bottomPanel.add(lblModel);
    }

    protected String[] showAndReturnForecastDetails() {
        boolean show = ckbForecast.isSelected();
        //Graph legend
        boxModel.setVisible(show);
        boxForecastData.setVisible(show);
        lblModel.setVisible(show);
        lblForecastData.setVisible(show);
        //Forecast into
        lblForecastInfo.setVisible(show);
        txtForecastInfo.setVisible(show);


        //Forecast
        return graph.showAndReturnForecastDetails(Integer.parseInt(txtDaysToForecast.getText()));
    }

    private JTextArea textAreaSetup() {
        JTextArea teatArea = new JTextArea();
        teatArea.setLineWrap(true);
        teatArea.setWrapStyleWord(true);
        teatArea.setOpaque(false);
        teatArea.setEditable(false);
        teatArea.setHighlighter(null);
        return teatArea;
    }
}