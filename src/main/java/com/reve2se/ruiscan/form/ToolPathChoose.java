package com.reve2se.ruiscan.form;

import com.reve2se.ruiscan.model.DB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;

public class ToolPathChoose {
    public static Map<String, Object> configObj;
    private  static ArrayList<JCheckBox> checkBoxList;
    public JPanel ToolPathChose;
    private JLabel enscanPathLabel;
    private JTextField enscanTextField;
    private JButton chooseEnscanButton;
    private JLabel oneforallPathLabel;
    private JTextField oneforallTextField;
    private JButton chooseOneforallButton;
    private JLabel jsfinderPathLabel;
    private JTextField jsfinderTextField;
    private JButton chooseJsfinderButton;
    private JLabel ciprPathLabel;
    private JTextField ciprTextField;
    private JButton chooseCiprButton;
    private JCheckBox downloadEnscan;
    private JCheckBox downloadJsfinder;
    private JCheckBox downloadCipr;
    private JPanel autoDownloadTools;
    private JCheckBox downloadOneforall;
    private JRadioButton darwinArmRadioButton;
    private JRadioButton darwinAmdRadioButton;
    private JRadioButton windowsAmd64RadioButton;
    private JRadioButton linuxAmd64RadioButton;
    private JTextField showDownloadDirPath;
    private JButton confirmDownloadPath;
    private JLabel chooseDoanloadFile;
    private JButton downloadButton;
    private JButton unzipButton;
    private JButton loadToolButton;
    private JLabel loadTextLabel;
    private JPanel osTypePanel;
    private JPanel toolNmae;
    private JButton confirmDownloadToolsButton;
    private JPanel chooseToolsJpanel;
    private JButton chooseToolsDirConfirm;
    private JLabel messgaeShow;
    private JTextField xrayTextField;
    private JButton chooseXrayButton;
    private JLabel xrayPathLabel;
    private JCheckBox xrayCheckBox;
    private JTextField fofaMapTextField;
    private JButton chooseFofaMapButton;
    private JLabel fofaMapPathLeabel;
    private JTextField fscanTextField;
    private JButton chooseFscanButton;
    private static String osType = "windows-amd64";
    private static String chooseEnscanPath;
    private static String chooseOneforallPath;
    private static String chooseJsfinderPath;
    private static String chooseCiprPath;
    private static String chooseXrayPath;
    private static String chooseFofaMapPath;
    private static String chooseFscanPath;


    private void chooseToolDir() {
        System.out.println("database is already create");
        chooseEnscanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String enscanFilePath = chooser.getSelectedFile().getAbsolutePath();
                    chooseEnscanPath = enscanFilePath;
                    enscanTextField.setText(enscanFilePath);
                    System.out.println("you choose enscan path is" + enscanFilePath);
                } else {
                    enscanTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseOneforallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String oneforallFilePath = chooser.getSelectedFile().getAbsolutePath();
                    oneforallTextField.setText(oneforallFilePath);
                    chooseOneforallPath = oneforallFilePath;
                    System.out.println("you choose OneForAll path is" + oneforallFilePath);
                } else {
                    oneforallTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseJsfinderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String jsfinderFilePath = chooser.getSelectedFile().getAbsolutePath();
                    jsfinderTextField.setText(jsfinderFilePath);
                    chooseJsfinderPath = jsfinderFilePath;
                    System.out.println("you choose JsFidner path is" + jsfinderFilePath);
                } else {
                    jsfinderTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseCiprButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String ciprFilePath = chooser.getSelectedFile().getAbsolutePath();
                    ciprTextField.setText(ciprFilePath);
                    chooseCiprPath = ciprFilePath;
                    System.out.println("you choose cIPR path is" + ciprFilePath);
                } else {
                    ciprTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseXrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String xrayFilePath = chooser.getSelectedFile().getAbsolutePath();
                    xrayTextField.setText(xrayFilePath);
                    chooseXrayPath = xrayFilePath;
                    System.out.println("you choose xray path is" + xrayFilePath);
                } else {
                    xrayTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseFofaMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String fofaMapFilePath = chooser.getSelectedFile().getAbsolutePath();
                    fofaMapTextField.setText(fofaMapFilePath);
                    chooseFofaMapPath = fofaMapFilePath;
                    System.out.println("you choose FofaMap path is" + fofaMapFilePath);
                } else {
                    fofaMapTextField.setText("??????????????????????????????");
                }
            }
        });
        chooseFscanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ToolPathChose);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String fscanFilePath = chooser.getSelectedFile().getAbsolutePath();
                    fscanTextField.setText(fscanFilePath);
                    chooseFscanPath = fscanFilePath;
                    System.out.println("you choose fscan path is" + fscanFilePath);
                } else {
                    fscanTextField.setText("??????????????????????????????");
                }
            }
        });
    }

    private void tips() {
        JOptionPane.showMessageDialog(ToolPathChose, "??????????????????????????????????????????????????????????????????????????????????????????\n???????????????????????????????????????????????????????????????????????????????????????ruiscan.db??????????????????\n?????????????????????????????????????????????????????????");
    }
    private void saveChooseDir2Db() {

        chooseToolsDirConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tips();
                DB data = new DB();
                data.setLastEnscanPath(chooseEnscanPath);
                data.setLastOneforallPath(chooseOneforallPath);
                data.setLastJsfinderPath(chooseJsfinderPath);
                data.setLastCiprPath(chooseCiprPath);
                data.setLastXrayPath(chooseXrayPath);
                data.setLastFofaMapPath(chooseFofaMapPath);
                data.setLastFscanPath(chooseFscanPath);

                try {
                    Files.write(Paths.get("ruiscan.db"), data.getDB().getBytes());
                    System.out.println("command in the try is excetued");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainForm.instance.loadRuiscan(chooseEnscanPath, chooseOneforallPath, chooseJsfinderPath, chooseCiprPath, chooseXrayPath, chooseFofaMapPath, chooseFscanPath);
            }
        });
    }
    private void initSavePath() {
        confirmDownloadPath.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(autoDownloadTools);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String toolDownloadPath = chooser.getSelectedFile().getAbsolutePath();
                    showDownloadDirPath.setText(toolDownloadPath);
                } else {
                    showDownloadDirPath.setText("??????????????????????????????");
                }
        });
    }

    private void initOsType() {
        windowsAmd64RadioButton.setSelected(true);
    }

    public void initSPDownloads() {
//        downloadButton.addActionListener(e -> new Thread(() -> {
        downloadButton.addActionListener(e -> {
            if (windowsAmd64RadioButton.isSelected()) {
                osType = "windows-amd64";
            } else if (linuxAmd64RadioButton.isSelected()) {
                osType = "linux-amd64";
                System.out.println("?????????????????????Linux-amd64");
            } else if (darwinAmdRadioButton.isSelected()) {
                osType = "darwin-amd";
            } else if (darwinArmRadioButton.isSelected()) {
                osType = "darwin-arm";
            } else {
                return;
            }
        });
    }

    public void initDownloadToolsChoose(){
        confirmDownloadToolsButton.addActionListener(e -> {
            if (downloadEnscan.isSelected()) {
                System.out.println("Download ENScan Checkbox has choosed");
                System.out.println(osType);
            }
            if (downloadJsfinder.isSelected()) {
                System.out.println("Download JsFinder Checkbox has choosed");
                System.out.println(osType);
            }
            if (downloadOneforall.isSelected()) {
                System.out.println("Download OneForAll Checkbox has choosed");
                System.out.println(osType);
            }
            if (downloadCipr.isSelected()) {
                System.out.println("Download cIPR Checkbox has choosed");
                System.out.println(osType);
            }

        });
    }

//    public static void startToolPathChoose() {
//        JFrame frame = new JFrame("ToolPathChoose");
//        frame.setContentPane(new ToolPathChoose().ToolPathChose);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }

    public ToolPathChoose() {
        chooseToolDir();
        saveChooseDir2Db();
        initOsType();
        initSavePath();
        initSPDownloads();
        initDownloadToolsChoose();

    }
}
