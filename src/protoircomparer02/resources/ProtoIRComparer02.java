/*
 * Aaron B. League, 6.15.2016
 *
 * This program is designed as a prototype for comparing frequencies from
 * Gaussian output files against experimental spectra. This code employs the
 * jcommon and jfreechart libraries, which are used to generate the plot, as 
 * well as OkHttp3 and JSON for communicating with the OSF API.
 * 
 * Utility contained in each file is as follows:
 * --ProtoIRComparer02.java contains the main method, as well as code for the 
 *   primary GUI frame and for the plot window
 * --Upload.java contains the GUI frame for uploading files to OSF
 * --Download.java contains the GUI frame for downloading files from OSF
 * --UpdateDefs.java contains the code for updating the list of files and
 *   metadata from the OSF repository
 * --UserAuth.java allows users to enter their own OSF credentials
 */
package protoircomparer02.resources;

import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * This class creates the Cartesian plot, given the input from ProtoIRComparer.
 * It is called once the GENERATE PLOT button in the GUI is pushed.
 *
 * @author Aaron B. League
 */
class Plot extends javax.swing.JFrame {

    //instantiate variables to hold information from ProtoIRComparer
    public boolean expt;
    public double[][] tempTheInten, tempExpInten;
    public static int theoNum;
    public int num;
    public double[] maxt;

    //assembles data into plot
    public Plot(double[][] TempTheInten, boolean Expt, double[][] TempExpInten,
            int Num, double[] MaxT) {

        super("ProtoIRComparer02");

        //initialize class variables
        tempTheInten = TempTheInten;
        tempExpInten = TempExpInten;
        expt = Expt;
        num = Num;
        maxt = MaxT;

        //initialize the chart
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Theory vs. Experiment",
                "Frequency",
                "Intensity",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        //list of colors to use in charts
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.CYAN, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE};

        //initialize chart variables
        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        chartPanel.getChart().getXYPlot().getDomainAxis().setInverted(true);
        final XYPlot plot = xylineChart.getXYPlot();

        //initialize renderer and data series look and feel
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int s = 0; s < theoNum; s++) {
            renderer.setSeriesPaint(s, colors[s]);
            renderer.setSeriesStroke(s, new BasicStroke());
        }
        if (expt == true) {
            renderer.setSeriesPaint(theoNum, Color.BLACK);
            renderer.setSeriesStroke(theoNum, new BasicStroke());
        }
        plot.setRenderer(renderer);
        renderer.setShapesVisible(false);
        setContentPane(chartPanel);
    }

    //creates the data set to hold the series that will go in the plot
    private XYDataset createDataset() {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        ArrayList<XYSeries> theory = new ArrayList();

        //populates a data series for each theory input
        for (int o = 0; o < theoNum; o++) {
            theory.add(new XYSeries("Theory" + (o + 1)));
            for (int p = 0; p < 4000; p++) {
                theory.get(o).add(p, tempTheInten[p][o]);
            }
            dataset.addSeries(theory.get(o));
        }

        //populates a data series for the experimental input, if applicable
        if (expt) {
            final XYSeries experiment = new XYSeries("Experiment");
            for (int q = 0; q < num; q++) {
                experiment.add(tempExpInten[q][0], tempExpInten[q][1]);
            }
            dataset.addSeries(experiment);
        }
        return dataset;
    }
}

/**
 * This is the main class, which handles the GUI and actions within it.
 * Functions of this class include reading in files, selecting options for the
 * plot, broadening peaks from the theory spectra, and populating the data
 * arrays that will be read into the Plot object.
 *
 * @author Aaron
 */
public class ProtoIRComparer02 extends javax.swing.JFrame {

    static int inputNum = 1;
    static double width = 4.0;
    protected static File[] Files = new File[10];
    double[][] scaleNum = {{1, 0.956}, {1, 0.956}, {1, 0.956}, {1, 0.956},
    {1, 0.956}, {1, 0.956}, {1, 0.956}, {1, 0.956}, {1, 0.956}, {0, 1}};

    /**
     * Creates new form ProtoIRComparer02
     */
    public ProtoIRComparer02() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        inputLab = new javax.swing.JLabel();
        inputBox = new javax.swing.JComboBox<>();
        intenScaleLab = new javax.swing.JLabel();
        intenScale = new javax.swing.JTextField();
        freqScaleLab = new javax.swing.JLabel();
        freqScale = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        filePreview = new javax.swing.JTextArea();
        delStruxBut = new javax.swing.JButton();
        genPlot = new javax.swing.JButton();
        isExpChk = new javax.swing.JCheckBox();
        combThe = new javax.swing.JCheckBox();
        widthScaleLab = new javax.swing.JLabel();
        widthScale = new javax.swing.JTextField();
        baseChk = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        OpenOSF = new javax.swing.JMenuItem();
        Upload = new javax.swing.JMenuItem();
        enterID = new javax.swing.JMenuItem();
        Quit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IR Comparer Prototype Version 2.0");
        setResizable(false);

        inputLab.setText("INPUT#");

        inputBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "Experiment" }));
        inputBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBoxActionPerformed(evt);
            }
        });

        intenScaleLab.setText("Scale Intensity:");

        intenScale.setText("1.0");
        intenScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intenScaleActionPerformed(evt);
            }
        });

        freqScaleLab.setText("Scale Frequencies:");

        freqScale.setText("0.956");
        freqScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freqScaleActionPerformed(evt);
            }
        });

        filePreview.setColumns(20);
        filePreview.setRows(5);
        jScrollPane1.setViewportView(filePreview);

        delStruxBut.setText("Delete Structure");
        delStruxBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delStruxButActionPerformed(evt);
            }
        });

        genPlot.setText("GENERATE PLOT");
        genPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genPlotActionPerformed(evt);
            }
        });

        isExpChk.setSelected(true);
        isExpChk.setText("Include Experiment");

        combThe.setText("Combine Theory Spectra");

        widthScaleLab.setText("HWHM (/cm):");

        widthScale.setText("4.0");
        widthScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                widthScaleActionPerformed(evt);
            }
        });

        baseChk.setText("Flatten Exp. Baseline");

        jMenu1.setText("File");

        Open.setText("Open (local)");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        OpenOSF.setText("Open (OSF)");
        OpenOSF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenOSFActionPerformed(evt);
            }
        });
        jMenu1.add(OpenOSF);

        Upload.setText("Upload (OSF)");
        Upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadActionPerformed(evt);
            }
        });
        jMenu1.add(Upload);

        enterID.setText("Enter OSF User ID");
        enterID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterIDActionPerformed(evt);
            }
        });
        jMenu1.add(enterID);

        Quit.setText("Quit");
        Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitActionPerformed(evt);
            }
        });
        jMenu1.add(Quit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(isExpChk)
                        .addGap(18, 18, 18)
                        .addComponent(combThe)
                        .addGap(18, 18, 18)
                        .addComponent(widthScaleLab)
                        .addGap(18, 18, 18)
                        .addComponent(widthScale, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(baseChk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(genPlot))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputLab)
                        .addGap(18, 18, 18)
                        .addComponent(inputBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(delStruxBut)
                        .addGap(135, 135, 135)
                        .addComponent(intenScaleLab)
                        .addGap(18, 18, 18)
                        .addComponent(intenScale, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(freqScaleLab)
                        .addGap(18, 18, 18)
                        .addComponent(freqScale, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputLab)
                    .addComponent(inputBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intenScaleLab)
                    .addComponent(intenScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freqScaleLab)
                    .addComponent(freqScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delStruxBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genPlot)
                    .addComponent(isExpChk)
                    .addComponent(combThe)
                    .addComponent(widthScaleLab)
                    .addComponent(widthScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(baseChk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        //This method uses the file chooser to help users select a file,
        //then shows the contents of the file in the text area
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                filePreview.read(new FileReader(file.getAbsolutePath()), null);
            } catch (IOException ex) {
                filePreview.setText("problem accessing file"
                        + file.getAbsolutePath());
            }
            //stores the file path in an array, to be read in later
            if (inputBox.getSelectedIndex() == inputNum) {
                Files[9] = file;
            } else if (Files[0] == null) {
                Files[0] = file;
                inputBox.setSelectedIndex(0);
            } else if (inputNum < 9) {
                Files[inputNum] = file;
                inputNum++;

                //adds an entry to the INPUT# menu, up to a maximum of 10
                inputBox.insertItemAt(String.valueOf(inputNum), inputNum - 1);
                inputBox.setSelectedIndex(inputNum - 1);
            } else {
                filePreview.setText("ALREADY AT 10 STRUCTURE LIMIT");
            }
        }
    }//GEN-LAST:event_OpenActionPerformed

    private void QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitActionPerformed
        //This is a redundancy, really. It makes the File > Quit work.
        //The user may instead simply close the window for the same effect.
        System.exit(0);
    }//GEN-LAST:event_QuitActionPerformed

    private void delStruxButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delStruxButActionPerformed
        //removes a structure from the INPUT# list and the file array
        if (inputNum > 1 && inputBox.getSelectedIndex() != inputNum) {
            inputNum--;
            for (int aa = inputBox.getSelectedIndex(); aa < 8; aa++) {
                Files[aa] = Files[aa + 1];
            }
            inputBox.removeItemAt(inputNum);
            filePreview.setText("SPECTRUM REMOVED");
        } else if (inputBox.getSelectedIndex() == 0) {
            Files[0] = null;
            filePreview.setText("SPECTRUM REMOVED");
        } else {
            Files[9] = null;
            filePreview.setText("SPECTRUM REMOVED");
        }
    }//GEN-LAST:event_delStruxButActionPerformed

    private void SGFilter92(double[][] espect) {
        //Applies a 9 point quadratic Savitzky-Golay filter to the data

        for (int i = 4; i < espect.length - 4; i++) {
            espect[i - 4][1] = ((-21 * espect[i - 4][1]) + (14 * espect[i - 3][1])
                    + (39 * espect[i - 2][1]) + (54 * espect[i - 1][1])
                    + (59 * espect[i][1]) + (54 * espect[i + 1][1])
                    + (39 * espect[i + 2][1]) + (14 * espect[i + 3][1])
                    + (-21 * espect[i + 4][1])) / 231;
            espect[i - 4][0] = espect[i][0];
        }
    }

    private void subtractBaseline(double[][] espect) {

        SGFilter92(espect);

        int r = 250;
        ArrayList<Integer> ind = new ArrayList();
        int count = 0;
        int temp = 0;
        double min = Math.PI;
        double lim = (2 * r) / Math.abs(espect[1][0] - espect[0][0]);

        while (count < espect.length - 1) {
            for (int i = 1; i < lim; i++) {
                if (count + i < espect.length) {
                    double dx = Math.abs(espect[count + i][0] - espect[count][0]);
                    double dy = (espect[count][1] - espect[count + i][1]) * 1000;
                    double r2 = Math.pow(dx, 2) + Math.pow(dy, 2);
                    if (r2 < Math.pow(2 * r, 2)) {
                        double alph = 1 - (r2 / (2 * Math.pow(r, 2)));
                        double gam = Math.acos(alph) - Math.atan(dy / dx);

                        if (min > gam) {
                            min = gam;
                            temp = i;
                        }
                    }
                } else {
                    break;
                }
            }
            count = count + temp;
            ind.add(count);
            temp = 0;
            min = Math.PI;
        }

        double slope = 0;
        for (int i = 0; i < espect.length; i++) {
            if (ind.get(temp).equals(i) && temp < (ind.size() - 1)) {
                slope = (espect[ind.get(temp + 1)][1] 
                        - espect[ind.get(temp)][1]) / 
                        (espect[ind.get(temp + 1)][0] - espect[ind.get(temp)][0]);
                temp++;
            }else{
                espect[i][1] -= espect[ind.get(temp)][1] + (slope * (espect[i][0] - 
                        espect[ind.get(temp)][0]));
            }
        }
        
        for (int i = 0; i < ind.size(); i++) {
            espect[ind.get(i)][1] = 0;
        }
    }

    private void genPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genPlotActionPerformed

        //in case the input is faulty, or something fails (see catch at the end)
        try {

            Scanner input1, input2;

            //Read frequencies and intensities from chosen output file
            //Initialize variables
            ArrayList F = new ArrayList();
            ArrayList I = new ArrayList();
            String temp0, temp1, temp2, temp3, temp4, temp5;
            double[][] spect = new double[4000][inputNum];
            double inten = 0;
            double[] maxT = new double[10];
            maxT[9] = 1;
            double f, g, y;
            double c = -((2 * (Math.pow(width, 2))) / (Math.log(4)));

            filePreview.setText("");

            //reads in the theory files and store frequencies
            if (Files[0] != null) {
                for (int r = 0; r < inputNum; r++) {
                    F.clear();
                    I.clear();
                    filePreview.append("Reading file " + Files[r].getName()
                            + "\n");
                    try {
                        input1 = new Scanner(Files[r]);

                        while (input1.hasNext()) {
                            temp0 = input1.next();
                            if (temp0.equals("Frequencies")) {
                                input1.next();
                                temp2 = input1.next();
                                F.add(temp2);
                                temp3 = input1.next();
                                F.add(temp3);
                                temp4 = input1.next();
                                F.add(temp4);
                            }
                            if (temp0.equals("IR")) {
                                temp1 = input1.next();
                                if (temp1.equals("Inten")) {
                                    input1.next();
                                    temp3 = input1.next();
                                    I.add(temp3);
                                    temp4 = input1.next();
                                    I.add(temp4);
                                    temp5 = input1.next();
                                    I.add(temp5);
                                }
                            }
                        }

                        input1.close();
                    } catch (Exception exception) {
                        filePreview.setText("ERROR READING FILE "
                                + Files[r].getName());
                    }

                    if (F.size() != I.size()) {
                        filePreview.setText("ERROR IN NUMBER OF FREQS VS. "
                                + "INTENSITIES in file " + r);
                    }

                    //broaden peaks with Gaussians and sum them into a function
                    for (int i = 0; i < 4000; i++) {
                        for (int j = 0; j < F.size(); j++) {
                            f = Double.valueOf(F.get(j).toString())
                                    * scaleNum[r][1];
                            g = Double.valueOf(I.get(j).toString());
                            y = g * Math.exp((Math.pow(i - f, 2)) / (c));
                            inten = inten + y;
                        }
                        spect[i][r] = inten * scaleNum[r][0];
                        if (maxT[r] < inten * scaleNum[r][0]) {
                            maxT[r] = inten * scaleNum[r][0];
                        }
                        inten = 0;
                    }
                }
            } else {
                //Don't try to read in spectra if there aren't any
                Plot.theoNum = 0;
            }

            ArrayList EF = new ArrayList();
            ArrayList EI = new ArrayList();
            double maxE = 0;

            //reads in the experimental file
            if (isExpChk.isSelected()) {
                filePreview.append("Reading file " + Files[9].getName());

                try {
                    input2 = new Scanner(Files[9]);
                    String[] tag;

                    while (input2.hasNext()) {
                        if (!input2.next().endsWith(",")) {
                            tag = input2.next().split(",");
                            EF.add(tag[0]);
                            EI.add(tag[1]);
                            f = Double.valueOf(tag[1]);
                            if (maxE < f) {
                                maxE = f;
                            }
                        }
                    }

                    input2.close();
                } catch (Exception exception2) {
                    filePreview.setText("ERROR READING EXPERIMENTAL FILE");
                }
            } else {
                filePreview.append("No experimental file to read");
            }

            //summs theory spectra into one spectrum if box is selected
            if (Files[0] != null) {
                if (combThe.isSelected() && inputNum > 1) {
                    for (int l = 0; l < 4000; l++) {
                        for (int m = 1; m < inputNum; m++) {
                            spect[l][0] += spect[l][m];
                        }

                        //keeps track of the highest peak for scaling purposes
                        if (spect[l][0] > maxT[9]) {
                            maxT[9] = spect[l][0];
                        }
                    }
                    Plot.theoNum = 1;

                    //if box is not selected, finds the highest theory peak
                } else {
                    for (int w = 0; w < inputNum; w++) {
                        if (maxT[w] > maxT[9]) {
                            maxT[9] = maxT[w];
                        }
                    }
                    Plot.theoNum = inputNum;
                }
            }

            //populates an array for the experimental spectrum
            double[][] Espect = new double[EF.size()][2];
            if (isExpChk.isSelected()) {
                for (int k = 0; k < EF.size(); k++) {
                    f = Double.valueOf(EF.get(k).toString());
                    g = Double.valueOf(EI.get(k).toString());
                    Espect[k][0] = f * scaleNum[9][1];
                    if (scaleNum[9][0] == 0) {
                        Espect[k][1] = (maxT[9] * g) / maxE;
                    } else {
                        Espect[k][1] = scaleNum[9][0] * g;
                    }
                }

                if (baseChk.isSelected()) {
                    this.subtractBaseline(Espect);
                }
            }

            Plot chart = new Plot(spect, isExpChk.isSelected(), Espect, EF.size(), maxT);
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);

            //writes spectra to .csv files in working directory
            if (Files[0] != null) {
                for (int i = 0; i < inputNum; i++) {
                    try {
                        String[] outFile = Files[i].getName().split("out");
                        PrintWriter writer = new PrintWriter(outFile[0]
                                + "spectrum.csv", "UTF-8");
                        for (int j = 0; j < 4000; j++) {
                            writer.print(j + "," + Math.round(spect[j][i])
                                    + "\n");
                        }
                        writer.close();
                    } catch (IOException ex) {
                        filePreview.setText("ERROR WRITING FREQUENCY SPECTRUM "
                                + "FILE FOR " + Files[i].getName());
                    }
                }
            }

            //catches problems executing the GENERATE PLOT function
        } catch (Exception ex) {
            filePreview.setText("ERROR GENERATING PLOTS. \n"
                    + "PLEASE CHECK TO MAKE SURE THEORY AND EXPERIMENT ARE NOT "
                    + "SWITCHED.");
        }
    }//GEN-LAST:event_genPlotActionPerformed

    private void inputBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBoxActionPerformed
        //displays the file at the currently selected INPUT#, if applicable
        if (inputBox.getSelectedIndex() == inputNum) {
            if (Files[9] != null) {
                try {
                    filePreview.read(new FileReader(Files[9]), null);
                } catch (IOException ex) {
                    filePreview.setText("Error displaying experiment file.");
                }
            } else {
                filePreview.setText("");
            }
        } else if (Files[inputBox.getSelectedIndex()] != null) {
            try {
                filePreview.read(new FileReader(
                        Files[inputBox.getSelectedIndex()]), null);
            } catch (IOException ex) {
                filePreview.setText("Error displaying theory file #"
                        + (inputBox.getSelectedIndex() + 1));
            }
        } else {
            filePreview.setText("");
        }

        //displays the intensity scaling associated with INPUT#
        if (inputBox.getSelectedIndex() == inputNum && scaleNum[9][0] == 0) {
            intenScale.setText("[default]");
            freqScale.setText(String.valueOf(scaleNum[9][1]));
        } else if (inputBox.getSelectedIndex() == inputNum) {
            intenScale.setText(String.valueOf(scaleNum[9][0]));
            freqScale.setText(String.valueOf(scaleNum[9][1]));
        } else {
            intenScale.setText(String.valueOf(scaleNum[inputBox.getSelectedIndex()][0]));
            freqScale.setText(String.valueOf(scaleNum[inputBox.getSelectedIndex()][1]));
        }
    }//GEN-LAST:event_inputBoxActionPerformed

    private void intenScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intenScaleActionPerformed
        //saves intensity scaling factor for the selected INPUT#
        if (inputBox.getSelectedIndex() == inputNum) {
            scaleNum[9][0] = Float.valueOf(intenScale.getText());
            filePreview.setText("INTENSITY FOR INDEX "
                    + inputBox.getSelectedIndex() + " SET TO "
                    + intenScale.getText());
        } else {
            scaleNum[inputBox.getSelectedIndex()][0]
                    = Float.valueOf(intenScale.getText());
            filePreview.setText("INTENSITY FOR INDEX "
                    + inputBox.getSelectedIndex() + " SET TO "
                    + intenScale.getText());
        }
    }//GEN-LAST:event_intenScaleActionPerformed

    private void freqScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freqScaleActionPerformed
        //saves frequency scaling factor for the selected INPUT#
        if (inputBox.getSelectedIndex() == inputNum) {
            scaleNum[9][1] = Float.valueOf(freqScale.getText());
            filePreview.setText("WARNING: YOU HAVE ALTERED THE SCALING IN THE "
                    + "EXPERIMENTAL SPECTRUM! \n" + "FREQUENCIES FOR INDEX "
                    + inputBox.getSelectedIndex() + " WILL BE SCALED BY "
                    + freqScale.getText());
        } else {
            scaleNum[inputBox.getSelectedIndex()][1]
                    = Float.valueOf(freqScale.getText());
            filePreview.setText("FREQUENCIES FOR INDEX "
                    + inputBox.getSelectedIndex() + " WILL BE SCALED BY "
                    + freqScale.getText());
        }
    }//GEN-LAST:event_freqScaleActionPerformed

    private void UploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadActionPerformed
        // links to upload class (used for uploading files to OSF)
        new Upload().setVisible(true);
    }//GEN-LAST:event_UploadActionPerformed

    private void OpenOSFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenOSFActionPerformed
        // links to download class (used for uploading files to OSF)
        new Download().setVisible(true);
    }//GEN-LAST:event_OpenOSFActionPerformed

    private void widthScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_widthScaleActionPerformed
        // adjusts the Half Width at Half Max (HWHM, measured in wavenumbers)
        width = Double.valueOf(widthScale.getText());
    }//GEN-LAST:event_widthScaleActionPerformed

    private void enterIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterIDActionPerformed
        // asks for OSF user ID
        new UserAuth().setVisible(true);
    }//GEN-LAST:event_enterIDActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProtoIRComparer02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProtoIRComparer02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProtoIRComparer02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProtoIRComparer02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProtoIRComparer02().setVisible(true);
            }
        });

        //ask users for username and password
        new UserAuth().setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Open;
    private javax.swing.JMenuItem OpenOSF;
    private javax.swing.JMenuItem Quit;
    private javax.swing.JMenuItem Upload;
    private javax.swing.JCheckBox baseChk;
    private javax.swing.JCheckBox combThe;
    private javax.swing.JButton delStruxBut;
    private javax.swing.JMenuItem enterID;
    private javax.swing.JFileChooser fileChooser;
    protected static javax.swing.JTextArea filePreview;
    private javax.swing.JTextField freqScale;
    private javax.swing.JLabel freqScaleLab;
    private javax.swing.JButton genPlot;
    protected static javax.swing.JComboBox<String> inputBox;
    private javax.swing.JLabel inputLab;
    private javax.swing.JTextField intenScale;
    private javax.swing.JLabel intenScaleLab;
    private javax.swing.JCheckBox isExpChk;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField widthScale;
    private javax.swing.JLabel widthScaleLab;
    // End of variables declaration//GEN-END:variables
}
