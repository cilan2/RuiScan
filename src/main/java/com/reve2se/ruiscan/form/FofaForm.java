package com.reve2se.ruiscan.form;

import com.reve2se.ruiscan.model.DB;
import com.reve2se.ruiscan.utils.DirUtil;
import com.reve2se.ruiscan.utils.ExecUtil;
import com.reve2se.ruiscan.utils.OsUtil;
import com.reve2se.ruiscan.utils.StringUtil;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
    private JCheckBox kingdeeCheckBox;
    private JTextArea fofaMapOutputTextArea;
    private JButton builtInCommandButton;
    private JButton fofaMapChooseDomainFileButton;
    private JTextField userRulesTextField;
    private JComboBox FofaResCountComboBox;
    private DB db;
    private String fofaMapConfigPath;
    private String fofaMapExecFilePath;
    private String fofaMapDomainFilePath;
    private volatile boolean stop = false;
    public List<String> checkBoxConfirm = new ArrayList<>();
    public String fofaMapRes;
    private static final Map<String, String> rulesMap = new HashMap<String, String>();
    static{
        rulesMap.put("shiro", "header=\"rememberme=deleteMe\" || header=\"shiroCookie\"");
        rulesMap.put("fastjson", "");
        rulesMap.put("ueditor", "app=\"百度-UEditor\" && server=\"Microsoft-IIS/7.5\"");
        rulesMap.put("Spring-boot", "app=\"vmware-SpringBoot-framework\"");
        rulesMap.put("weaverOA", "header=\"Set-Cookie: ecology_JSessionId=\" || app=\"泛微-EMobile\"");
        rulesMap.put("seeyon", "title =\"A8\" || app=\"致远互联-A8-V5\" || app=\"致远互联-OA\"");
        rulesMap.put("yonyouOA", "app=\"用友-UFIDA-NC\"");
        rulesMap.put("tongdaOA", "app=\"TDXK-通达OA\"");
        rulesMap.put("kingdee", "body=\"easSessionId\" || body=\"/kdgs/script/kdgs.js\"");
    }

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
                fofaMapOutputTextArea.append("\n");
                StringUtil.autoReplace(fofaMapConfigPath, "size = 100", "size = " + FofaResCountComboBox.getSelectedItem());
                fofaMapOutputTextArea.append("已将最大查询数写入fofa.ini");
                fofaMapOutputTextArea.append("\n");
                StringUtil.autoReplace(fofaMapExecFilePath, "config.read('fofa.ini', encoding=\"utf-8\")", "config.read('" + fofaMapConfigPath + "', encoding=\"utf-8\")");
                String fofaExec1 = new String(DirUtil.dirStructure(new String[]{db.getLastFofaMapPath().substring(0,db.getLastFofaMapPath().lastIndexOf("/")), "fofa.py"}));
                StringUtil.autoReplace(fofaExec1, "config.read('fofa.ini', encoding=\"utf-8\")", "config.read('" + fofaMapConfigPath + "', encoding=\"utf-8\")");
                fofaMapOutputTextArea.append("已改写配置文件路径为绝对路径");
                fofaMapOutputTextArea.append("\n");
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
                System.out.println(rulesMap.get("shiro"));
            }
            if (fastjsonCheckBox.isSelected()) {
                checkBoxConfirm.add("fastjson");
                System.out.println(rulesMap.get("fastjson"));
            }
            if (ueditorCheckBox.isSelected()) {
                checkBoxConfirm.add("ueditor");
                System.out.println(rulesMap.get("ueditor"));
            }
            if (springBootCheckBox.isSelected()) {
                checkBoxConfirm.add("Spring-boot");
                System.out.println(rulesMap.get("Spring-boot"));
            }
            if (weaverOACheckBox.isSelected()) {
                checkBoxConfirm.add("weaverOA");
                System.out.println(rulesMap.get("weaverOA"));
            }
            if (seeyonOACheckBox.isSelected()) {
                checkBoxConfirm.add("seeyon");
                System.out.println(rulesMap.get("seeyon"));
            }
            if (yonyouOACheckBox.isSelected()) {
                checkBoxConfirm.add("yonyouOA");
                System.out.println(rulesMap.get("yonyouOA"));
            }
            if (tongdaOACheckBox.isSelected()) {
                checkBoxConfirm.add("tongdaOA");
                System.out.println(rulesMap.get("tongdaOA"));
            }
            if (kingdeeCheckBox.isSelected()) {
                checkBoxConfirm.add("kingdee");
                System.out.println(rulesMap.get("kingdee"));
            }
            // 组合指纹构建
            String rules = new String("");
            for (int i = 0; i < checkBoxConfirm.size(); i++) {
                rules = rules + " || " + rulesMap.get(checkBoxConfirm.get(i));
            }
            rules = rules.replaceFirst(" \\|\\| ", "");

            // fofaMap执行逻辑
            if (StringUtil.notEmpty(fofaDomainFileTextField.getText())) {
            // 如果输入是一个domain file的话就在当前的res目录下再创建一个fofaMapRes目录用于存放结果
                fofaMapRes = DirUtil.dirStructure(new String[]{MainForm.resPath, "fofaMapRes"});
                String[] fofaMkdirExecCmd = new String[]{"mkdir", fofaMapRes};
                execAndFresh(fofaMkdirExecCmd);
            // 从目标文件中逐行读取域名来给fofaMap执行，并且指定输出文件的名字和路径
                try {
                    // create a reader instance
                    BufferedReader br = new BufferedReader(new FileReader(fofaDomainFileTextField.getText()));
                    // read until end of file
                    String line;
                    while ((line = br.readLine()) != null) {
                        fofaMapOutputTextArea.append("规定的指纹规则为 ---> " + "domain=\"" + line + "\" && (" + rules + ")");
                        Thread.sleep(2000);
                        String[] fofaExecCmd = new String[]{"python", fofaMapExecFilePath, "-q", "domain=\"" + line + "\" && (" + rules + ")", "-o", DirUtil.dirStructure(new String[]{fofaMapRes, line.replace(".", "_") + ".xlsx "})};
                        execAndFresh(fofaExecCmd);
                    }
                    // close the reader
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }



            } else if (StringUtil.notEmpty(fofaSingleDomainTextField.getText())) {
                String[] fofaExecCmd = new String[]{"python", fofaMapExecFilePath, "-q", "domain=\"" + fofaSingleDomainTextField.getText() + "\" && (" + userRulesTextField.getText() + ")", "-o", DirUtil.dirStructure(new String[]{MainForm.resPath, fofaSingleDomainTextField.getText().replace(".", "_") + ".xlsx "})};
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
