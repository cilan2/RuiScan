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
    private static String osType = "windows-amd64";
    private static String chooseEnscanPath;
    private static String chooseOneforallPath;
    private static String chooseJsfinderPath;
    private static String chooseCiprPath;
    private static String chooseXrayPath;
    private static String chooseFofaMapPath;


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
                    enscanTextField.setText("你好像啥也没选。。。");
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
                    oneforallTextField.setText("你好像啥也没选。。。");
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
                    jsfinderTextField.setText("你好像啥也没选。。。");
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
                    ciprTextField.setText("你好像啥也没选。。。");
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
                    xrayTextField.setText("你好像啥也没选。。。");
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
                    fofaMapTextField.setText("你好像啥也没选。。。");
                }
            }
        });
    }

    private void tips() {
        JOptionPane.showMessageDialog(ToolPathChose, "点击确定之后会将工具路径写入数据库以供下次打开软件时自动加载\n如果没有选中全部工具路径会导致数据库中路径不完整，需要删除ruiscan.db重新选择生成\n重新选择后点击数据库会重制为最新的选择");
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

                try {
                    Files.write(Paths.get("ruiscan.db"), data.getDB().getBytes());
                    System.out.println("command in the try is excetued");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainForm.instance.loadRuiscan(chooseEnscanPath, chooseOneforallPath, chooseJsfinderPath, chooseCiprPath, chooseXrayPath, chooseFofaMapPath);
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
                    showDownloadDirPath.setText("你好像啥也没选。。。");
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
                System.out.println("系统架构已选择Linux-amd64");
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
