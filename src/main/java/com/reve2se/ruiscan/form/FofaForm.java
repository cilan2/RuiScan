package com.reve2se.ruiscan.form;

import com.reve2se.ruiscan.model.DB;
import com.reve2se.ruiscan.utils.DirUtil;
import com.reve2se.ruiscan.utils.ExecUtil;
import com.reve2se.ruiscan.utils.OsUtil;
import com.reve2se.ruiscan.utils.StringUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FofaForm {
    public JPanel Fofa;
    private JPanel FofaLoginInfo;
    private JTextField fofaEmailTextField;
    private JTextField fofaKeyTextField;
    private JButton confirmFofaLoginButton;
    private JTextField fofaDomainFileTextField;
    private JTextField fofaSingleDomainTextField;
    private JPanel FingerprintScreening;
    private JCheckBox shiroCheckBox;
    private JCheckBox fastjsonCheckBox;
    private JCheckBox ueditorCheckBox;
    private JCheckBox springBootCheckBox;
    private JCheckBox seeyonOACheckBox;
    private JCheckBox weaverOACheckBox;
    private JCheckBox yonyouOACheckBox;
    private JCheckBox tongdaOACheckBox;
    private JTextArea fofaMapOutputTextArea;
    private JButton builtInCommandButton;
    private JButton fofaMapChooseDomainFileButton;
    private JTextField textField1;
    private JButton 确认Button;
    private JComboBox FofaResCountComboBox;
    private JCheckBox kingdeeCheckBox;
    private DB db;
    private String fofaMapConfigPath;
    private String fofaMapExecFilePath;
    private String fofaMapDomainFilePath;
    private volatile boolean stop = false;
    public List<String> checkBoxConfirm = new ArrayList<>();

    //  一个被封装好的执行命令
    private void execAndFresh(String[] finalCmd) {
//      将outputTextArea清空
//        outputTextArea.setText(null);
//      创建一个新的并发
        Thread thread = new Thread(() -> {
            try {

                Process process = ExecUtil.exec(finalCmd);
                if (process == null) {
                    fofaMapOutputTextArea.append("process == null");
                    return;
                }
                InputStream inputStream = process.getInputStream();
                if (inputStream == null) {
                    fofaMapOutputTextArea.append("inputStream == null");
                    return;
                }
                BufferedReader isReader;
                if (OsUtil.isWindows()) {
                    InputStreamReader isr = new InputStreamReader(inputStream,
                            StandardCharsets.UTF_8);
                    isReader = new BufferedReader(isr);
                } else {
                    isReader = new BufferedReader(new InputStreamReader(inputStream));
                }
                String thisLine;
                while ((!stop) && (thisLine = isReader.readLine()) != null) {
                    System.out.println(thisLine);
                    String cmd = String.join(" ", finalCmd);
//                  将输出的结果Put进outputTextArea
                    fofaMapOutputTextArea.append(thisLine);
                    fofaMapOutputTextArea.append("\n");
//                    fofaDomainFileTextField.setCaretPosition(fofaMapOutputTextArea.getText().length());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

//    获取FofaMap相关路径
    public void initFofaMap() {
        try {
            Path dbPath = Paths.get("ruiscan.db");
            if (Files.exists(dbPath)) {
                byte[] data = Files.readAllBytes(dbPath);
                db = DB.parseDB(data);
                fofaMapExecFilePath = db.getLastFofaMapPath();
                String[] fofaMapTest = new String[]{"python", fofaMapExecFilePath};
                execAndFresh(fofaMapTest);
                fofaMapOutputTextArea.append("\n");
                if (StringUtil.notEmpty(MainForm.outputFileConpanyName)) {
                    fofaMapOutputTextArea.append("当前的任务名称为：" + MainForm.outputFileConpanyName);
                    System.out.println("当前的任务名称为：" + MainForm.outputFileConpanyName);
                } else {
                    fofaMapOutputTextArea.append("当前的任务名称为：" + MainForm.startName);
                    System.out.println("当前的任务名称为：" + MainForm.startName);
                }
                if (OsUtil.isWindows()) {
                    fofaMapConfigPath = DirUtil.dirStructure(new String[]{db.getLastFofaMapPath().substring(0, db.getLastFofaMapPath().lastIndexOf("\\")), "fofa.ini"});
                } else {
                    fofaMapConfigPath = DirUtil.dirStructure(new String[]{db.getLastFofaMapPath().substring(0,db.getLastFofaMapPath().lastIndexOf("/")), "fofa.ini"});
                }
            } else {
                JOptionPane.showMessageDialog(Fofa, "请确认FofaMap工具路径配置正确");
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

//        将用户设置的fofa_email和fofa_key写入fofa.ini文件
        confirmFofaLoginButton.addActionListener(e ->  {
            if (StringUtil.notEmpty(fofaEmailTextField.getText())&&StringUtil.notEmpty(fofaKeyTextField.getText())) {
                StringUtil.autoReplace(fofaMapConfigPath, "email = xxxx", "email = " + fofaEmailTextField.getText());
                fofaMapOutputTextArea.append("已将email信息写入fofa.ini");
                fofaMapOutputTextArea.append("\n");
                StringUtil.autoReplace(fofaMapConfigPath, "key = xxxxxxxxxx", "key = " + fofaKeyTextField.getText());
                fofaMapOutputTextArea.append("已将key信息写入fofa.ini");
                StringUtil.autoReplace(fofaMapExecFilePath, "config.read('fofa.ini', encoding=\"utf-8\")", "config.read('" + fofaMapConfigPath + "', encoding=\"utf-8\")");
                String fofaExec1 = new String(DirUtil.dirStructure(new String[]{db.getLastFofaMapPath().substring(0,db.getLastFofaMapPath().lastIndexOf("/")), "fofa.py"}));
                StringUtil.autoReplace(fofaExec1, "config.read('fofa.ini', encoding=\"utf-8\")", "config.read('" + fofaMapConfigPath + "', encoding=\"utf-8\")");
                fofaMapOutputTextArea.append("\n");
                fofaMapOutputTextArea.append("已改写配置文件路径为绝对路径");
                String[] fofaMapTest = new String[]{"python", fofaMapExecFilePath};
                execAndFresh(fofaMapTest);

            } else {
                JOptionPane.showMessageDialog(Fofa, "请确认Fofa_email和Fofa_key均已正确输入");
            }
        });
    }

    public void initBuiltIn() {
//        让用户选择fofa执行的域名文件
        fofaMapChooseDomainFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            int option = chooser.showOpenDialog(Fofa);
            if (option == JFileChooser.APPROVE_OPTION) {
                fofaMapDomainFilePath = chooser.getSelectedFile().getAbsolutePath();
                fofaDomainFileTextField.setText(fofaMapDomainFilePath);
            } else {
                JOptionPane.showMessageDialog(Fofa, "你好像啥也没选。。。");
            }
        });

        builtInCommandButton.addActionListener(e ->  {
            //收集用户选择的内置指纹规则
            checkBoxConfirm.clear();
            if (shiroCheckBox.isSelected()) {
                checkBoxConfirm.add("shiro");
            }
            if (fastjsonCheckBox.isSelected()) {
                checkBoxConfirm.add("fastjson");
            }
            if (ueditorCheckBox.isSelected()) {
                checkBoxConfirm.add("ueditor");
            }
            if (springBootCheckBox.isSelected()) {
                checkBoxConfirm.add("Spring-boot");
            }
            if (weaverOACheckBox.isSelected()) {
                checkBoxConfirm.add("weaverOA");
            }
            if (seeyonOACheckBox.isSelected()) {
                checkBoxConfirm.add("seeyon");
            }
            if (yonyouOACheckBox.isSelected()) {
                checkBoxConfirm.add("yonyouOA");
            }
            if (tongdaOACheckBox.isSelected()) {
                checkBoxConfirm.add("tongdaOA");
            }
            if (kingdeeCheckBox.isSelected()) {
                checkBoxConfirm.add("kingdee");
            }
            // fofaMap执行逻辑
            if (StringUtil.notEmpty(fofaDomainFileTextField.getText())) {
                String[] fofaExecCmd = new String[]{"python", fofaMapExecFilePath, "-bq", fofaMapDomainFilePath};
                execAndFresh(fofaExecCmd);
            } else if (StringUtil.notEmpty(fofaSingleDomainTextField.getText())) {
                String[] fofaExecCmd = new String[]{"python", fofaMapExecFilePath, "-q", fofaSingleDomainTextField.getText(), "-o", DirUtil.dirStructure(new String[]{"res", fofaSingleDomainTextField.getText().replace(".", "_") + ".xlsx "})};
                execAndFresh(fofaExecCmd);
            } else {
                JOptionPane.showMessageDialog(Fofa, "请选择一个目标");
            }
        });
    }

    public FofaForm() {
        initFofaMap();
        initBuiltIn();
    }
}
