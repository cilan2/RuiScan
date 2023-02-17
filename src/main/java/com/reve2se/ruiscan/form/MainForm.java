package com.reve2se.ruiscan.form;

import com.reve2se.ruiscan.model.DB;
import com.reve2se.ruiscan.utils.ExecUtil;
import com.reve2se.ruiscan.utils.OsUtil;
import com.reve2se.ruiscan.utils.StringUtil;
import com.reve2se.ruiscan.model.Excel2Txt;
import com.reve2se.ruiscan.utils.DirUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    public static MainForm instance;
    private JPanel RuiScan;
    private JPanel enscanJpanel;
    private JPanel jsfinderJpanel;
    private JPanel oneforallJpanel;
    private JTextField ensTargetCompany;
    private JButton ensCompanyConfirm;
    private JButton openResultDirButton;
    private JButton outputDomainButton;
    private JTextField jsfinderChooseDomainDirTextField;
    private JButton jsfinderChooseDomainDirButton;
    private JTextField jsfinderSingleDomainTextField;
    private JButton jsfinderChooseSingleDomainButton;
    private JTextField oneforallChooseDomainDirTextField;
    private JButton oneforallChooseDomainDirButton;
    private JTextField oneforallChooseSingleDomainTextField;
    private JButton oneforallChooseSingleDomianButton;
    private JButton toolPathChoose;
    private JTextArea outputTextArea;
    private JPanel ciprJpanel;
    private JTextField ciprDomainDirTextField;
    private JButton ChooseCiprDomainDirButton;
    private JButton execOneForAllButton;
    private JButton execJsFinderButton;
    private JButton execCiprButton;
    private JButton execENScanButton;
    private JButton startFingerButton;
    private JButton reloadPocButton;
    private JButton showAllPocButton;
    private JTextField choosePocTextField;
    private JButton pocConfirmButton;
    private JButton targetConfirmButton;
    private JButton startExecPocButton;
    private JButton onekeyAutoButton;
    private JPanel execPoc;
    private JPanel fingerTool;
    private JLabel showPocSumLabel;
    private JLabel showAlivableTarget;
    private JPanel alreadyChooseToolsDirJpanel;
    private JTextField showEnscanFileChoose;
    private JTextField showOneforallFileChoose;
    private JTextField showJsfinderFileChoose;
    private JTextField showCiprFileChoose;
    private JLabel enscanFileChoose;
    private JLabel oneforallFileCho;
    private JLabel jsfinderFileChoose;
    private JLabel ciprFileChoose;
    private JScrollPane outputPanel;
    private JTextField ciprThreadTextField;
    private JButton openOneforallConfigDirButton;
    private JLabel XrayFileChoose;
    private JTextField showXrayFileChoose;
    private JTextField chooseExsitTextFiled;
    private JButton fofaButton;
    private JTextField showFofaMapFileChoose;
    private JRadioButton xrayRadioButton;
    private JRadioButton fscanRadioButton;
    private JTextField showFscanFileChoose;
    private static DB db;
    public static String enscanPathFull;
    public static String oneforallPathFull;
    public static String jsfinderPathFull;
    public static String ciprPathFull;
    public static String xrayPathFull;
    public static String fofaMapPathFull;
    public static String fscanPathFull;
    public static String companyNmae;
    public static String jsfinderTargetUrl;
    public static String ciprDomainDirPath;
    public static String oneforallTargetUrl;
    public static String jsfinderChooseDomainDir;
    public static String oneforallChooseDomainDir;
    public static String outputFileConpanyName;
    public static String currentPath;
    public static String resPath;
    public static String startPath;
    public static String startName;
    public static String domainFilePath;
    public static String ipFilePath;
    private volatile boolean stop = false;


//  处理配置文件
//  若存在ruiscan.db文件直接读取工具路径，若不存在初始化
    public void init() {
        File dictory = new File("");
        currentPath = dictory.getAbsolutePath();
        System.out.println("运行RuiScan的目录为：" + currentPath);
        resPath = DirUtil.dirStructure(new String[]{currentPath, "res"});
        try {
            Path dbPath = Paths.get("ruiscan.db");
            if (Files.exists(dbPath)) {
                byte[] data = Files.readAllBytes(dbPath);
                db = DB.parseDB(data);
            } else {
                db = new DB();
                db.setLastEnscanPath(null);
                db.setLastOneforallPath(null);
                db.setLastJsfinderPath(null);
                db.setLastCiprPath(null);
                db.setLastXrayPath(null);
                db.setLastFofaMapPath(null);
                db.setLastFscanPath(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                    outputTextArea.append("process == null");
                    return;
                }
                InputStream inputStream = process.getInputStream();
                if (inputStream == null) {
                    outputTextArea.append("inputStream == null");
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
                    if (cmd.contains("ENScan")) {
                        if (thisLine.contains("根据关键词查询到公司")) {
                            System.out.println(thisLine);
                            outputFileConpanyName = thisLine.toString().split(" ")[1];
                            System.out.println(outputFileConpanyName);
                        }
                    }
//                  将输出的结果Put进outputTextArea
                    outputTextArea.append(thisLine);
                    outputTextArea.append("\n");
                    outputTextArea.setCaretPosition(outputTextArea.getText().length());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

//  用另一种方式封装好的系统命令只执行函数
    private void execRunntime(String[] args) {
        Thread thread = new Thread(() -> {
            Process proc;
            try {
                proc = Runtime.getRuntime().exec(args);// 执行py文件
                //用输入输出流来截取结果
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line;
                outputTextArea.append("\n");
                outputTextArea.append("start exec execRuntime");
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    outputTextArea.append(line);
                    outputTextArea.append("\n");
                    outputTextArea.setCaretPosition(outputTextArea.getText().length());
                }
                in.close();
                proc.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

//  显示从数据库读到的工具路径过刚刚设置好的工具路径后进行初始化
    public void loadRuiscan(String enscanPath, String oneforallPath, String jsfinderPath, String ciprPath, String xrayPath, String fofaMapPath, String fscanPath) {
//        String targetDir = Paths.get(enscanPath).toFile().getParent() + File.separator;
        enscanPathFull = enscanPath;
        oneforallPathFull = oneforallPath;
        jsfinderPathFull = jsfinderPath;
        ciprPathFull = ciprPath;
        xrayPathFull = xrayPath;
        fofaMapPathFull = fofaMapPath;
        fscanPathFull = fscanPath;
        showEnscanFileChoose.setText(enscanPath);
        showOneforallFileChoose.setText(oneforallPath);
        showJsfinderFileChoose.setText(jsfinderPath);
        showCiprFileChoose.setText(ciprPath);
        showXrayFileChoose.setText(xrayPath);
        showFofaMapFileChoose.setText(fofaMapPath);
        showFscanFileChoose.setText(fscanPath);
//        outputTextArea.append("ENScan工具自检\n");
        String[] ensCmd = new String[]{"python", enscanPathFull, "--help"};
        execAndFresh(ensCmd);
        outputTextArea.setText(null);
//        outputTextArea.append("OneForAll工具自检\n");
        String[] oneforallCmd = new String[]{"python", oneforallPathFull, "version"};
        execAndFresh(oneforallCmd);
        outputTextArea.setText(null);
//        outputTextArea.append("JsFinder工具自检\n");
        String[] jsfindCmd = new String[]{"python", jsfinderPathFull, "--help"};
        execAndFresh(jsfindCmd);
        outputTextArea.setText(null);
//        outputTextArea.append("cIPR工具自检\n");
        String[] ciprCmd = new String[]{ciprPathFull};
        execAndFresh(ciprCmd);
//        outputTextArea.append("xray工具自检\n");
        String[] xrayCmd = new String[]{xrayPathFull, "version"};
        execAndFresh(xrayCmd);
//        outputTextArea.append("FofaMap工具自检\n");
        String[] fofaMapCmd = new String[]{"python", fofaMapPathFull};
        execAndFresh(fofaMapCmd);
//        outputTextArea.append("Fscan工具自检\n");
        String[] fscanCmd = new String[]{fscanPathFull};
        execAndFresh(fscanCmd);
    }

//  从本地配置文件中读取之前选定过的工具文件夹
    public void initLoadTools() {
        if (StringUtil.notEmpty(db.getLastFscanPath()) && StringUtil.notEmpty(db.getLastFofaMapPath()) && StringUtil.notEmpty(db.getLastXrayPath()) && StringUtil.notEmpty(db.getLastEnscanPath()) && StringUtil.notEmpty(db.getLastOneforallPath()) && StringUtil.notEmpty(db.getLastJsfinderPath()) && StringUtil.notEmpty(db.getLastCiprPath()) && !db.getLastEnscanPath().equals("null") && !db.getLastOneforallPath().equals("null") && !db.getLastJsfinderPath().equals("null") && !db.getLastCiprPath().equals("null") && !db.getLastXrayPath().equals("null") && !db.getLastFofaMapPath().equals("null") && !db.getLastFscanPath().equals("null")) {
            loadRuiscan(db.getLastEnscanPath(), db.getLastOneforallPath(), db.getLastJsfinderPath(), db.getLastCiprPath(), db.getLastXrayPath(), db.getLastFofaMapPath(), db.getLastFscanPath());
            System.out.println("if exec in initLoadTools");
            showEnscanFileChoose.setText(db.getLastEnscanPath());
            showOneforallFileChoose.setText(db.getLastOneforallPath());
            showJsfinderFileChoose.setText(db.getLastJsfinderPath());
            showCiprFileChoose.setText(db.getLastCiprPath());
            showXrayFileChoose.setText(db.getLastXrayPath());
            showFofaMapFileChoose.setText(db.getLastFofaMapPath());
            showFscanFileChoose.setText(db.getLastFscanPath());
        }
    }
//  打开工具路径选择对话框
    public void initToolPathChoose() {
        toolPathChoose.addActionListener(e -> {
                String t = "工具路径选择";
                JFrame frame = new JFrame(t);
                frame.setContentPane(new ToolPathChoose().ToolPathChose);
                frame.setResizable(true);
                frame.pack();
                frame.setVisible(true);
        });
    }

    public void initFofaForm() {
        fofaButton.addActionListener(e -> {
            String t = "FofaMap";
            JFrame frame = new JFrame(t);
            frame.setContentPane(new FofaForm().Fofa);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            frame.setSize(1280,500);
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize=kit.getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;
            final int WIDTH = 1280;
            final int HEIGHT = 500;
            int x=(width -WIDTH)/2;
            int y=(height - HEIGHT)/2;
            frame.setLocation(x,y);
        });
    }

    public void initExecEnscan() {
//      指定企业名称 --> enscan指定企业名称
        ensCompanyConfirm.addActionListener(e -> {
            companyNmae = ensTargetCompany.getText();
            System.out.println("choose target company is -> " + companyNmae);
            outputTextArea.setText(null);
            outputTextArea.append("Choose target company is -> " + companyNmae);
        });
//      执行enscan
        execENScanButton.addActionListener(e -> {
            String[] ensCmd = new String[]{"python", enscanPathFull, "-s", companyNmae};
            execAndFresh(ensCmd);

        });
//      打开结果文件夹
        openResultDirButton.addActionListener(e -> {
            if (!DirUtil.dirIsExist(resPath)) {
                JOptionPane.showMessageDialog(RuiScan, "当前还没有运行过任何任务，结果文件夹不存在");
            } else {
                if (OsUtil.isWindows()) {
                    execAndFresh(new String[]{"explorer", resPath});
                } else {
                    execAndFresh(new String[]{"open", resPath});
                }
            }
        });
//      从enscan结果中导出域名文件
        outputDomainButton.addActionListener(e -> {
            String companyPath = DirUtil.dirStructure(new String[]{resPath, outputFileConpanyName});
            Path path = Paths.get(companyPath);
            try {
                Path pathCreate = Files.createDirectory(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String excelPath = DirUtil.dirStructure(new String[]{currentPath, "res", timeString + "-" + outputFileConpanyName + ".xlsx"});
            Excel2Txt.excel2txt(excelPath, outputFileConpanyName);
            resPath = DirUtil.dirStructure(new String[]{currentPath, "res", outputFileConpanyName});
            domainFilePath = DirUtil.dirStructure(new String[]{resPath, "domain_" + outputFileConpanyName + ".txt"});
            ipFilePath = DirUtil.dirStructure(new String[]{resPath, "ip_" + outputFileConpanyName + ".txt"});

        });
    }

    public void initExecJsfinder() {
//      指定单个域名 --> 读取JsFinder指定的单个域名
        jsfinderChooseSingleDomainButton.addActionListener(e -> {
            jsfinderTargetUrl = jsfinderSingleDomainTextField.getText();
            outputTextArea.setText(null);
            outputTextArea.append("JsFinder 指定了单个域名 --> " + jsfinderTargetUrl);
        });
//      指定域名文件 --> 读取JsFinder指定的域名文件
        jsfinderChooseDomainDirButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            int option = chooser.showOpenDialog(jsfinderJpanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                jsfinderChooseDomainDir = chooser.getSelectedFile().getAbsolutePath();
                jsfinderChooseDomainDirTextField.setText(jsfinderChooseDomainDir);
            } else {
                jsfinderChooseDomainDirTextField.setText("你好像啥也没选。。。");
            }
        });
//      执行JsFinder -->
        execJsFinderButton.addActionListener(e ->  {
            if (StringUtil.notEmpty(jsfinderTargetUrl)) {
                String[] jsCmd = new String[]{"python", jsfinderPathFull, "-u", jsfinderTargetUrl, "-ou", DirUtil.dirStructure(new String[]{currentPath, "res", jsfinderTargetUrl.toString().replace(".", "_") + "_url.txt"}), "-os", DirUtil.dirStructure(new String[]{currentPath, "res", jsfinderTargetUrl.toString().replace(".", "_") + "_subdomain.txt"})};
                execAndFresh(jsCmd);
            } else if (StringUtil.notEmpty(jsfinderChooseDomainDir)) {
                String[] jsCmd = new String[]{"python", jsfinderPathFull, "-f", jsfinderChooseDomainDir, "-ou", resPath, "-os", resPath};
                System.out.println(String.join(" ", jsCmd));
                execAndFresh(jsCmd);
            } else {
                outputTextArea.append("please choose single domain or domain dir for target");
            }
        });
    }

    public void initExecCipr() {
//      选择域名文件 --> cIPR域名文件选择
        ChooseCiprDomainDirButton.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser(".");
                int option = chooser.showOpenDialog(ciprJpanel);
                if (option == JFileChooser.APPROVE_OPTION) {
                    ciprDomainDirPath = chooser.getSelectedFile().getAbsolutePath();
                    ciprDomainDirTextField.setText(ciprDomainDirPath);
                } else {
                    ciprDomainDirTextField.setText("你好像啥也没选。。。");
                }
        });
//      提取C段 --> 执行cIPR工具
        execCiprButton.addActionListener(e ->  {
//          如果自定义了线程就添加进命令
            if (StringUtil.notEmpty(ciprThreadTextField.getText())) {
                String[] ciprCmd = new String[]{ciprPathFull, "-t", ciprThreadTextField.getText(), ciprDomainDirPath};
                execAndFresh(ciprCmd);
            } else {
                String[] ciprCmd = new String[]{ciprPathFull, ciprDomainDirPath};
                execAndFresh(ciprCmd);
            }
        });
    }

    public void initExecOneforall() {
//      指定单个域名 -->  OneForAll读取指定的域名
        oneforallChooseSingleDomianButton.addActionListener(e -> {
            oneforallTargetUrl = oneforallChooseSingleDomainTextField.getText();
            outputTextArea.setText(null);
            outputTextArea.append("OneForAll 指定了单个域名 --> " + oneforallTargetUrl);
        });

//      指定域名文件 -->  OneForAll读取指定域名文件
        oneforallChooseDomainDirButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(".");
            int option = chooser.showOpenDialog(oneforallJpanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                oneforallChooseDomainDir = chooser.getSelectedFile().getAbsolutePath();
                oneforallChooseDomainDirTextField.setText(oneforallChooseDomainDir);
            } else {
                oneforallChooseDomainDirTextField.setText("你好像啥也没选。。。");
            }
        });

//      执行OneForAll --> 调用python命令执行OneForAll
        execOneForAllButton.addActionListener(e -> {
            if (StringUtil.notEmpty(oneforallChooseSingleDomainTextField.getText())) {
                outputTextArea.setText(null);
                outputTextArea.append("\n");
                outputTextArea.append("开始执行OneForAll，命令为 --> python " + oneforallPathFull + " --target " + oneforallTargetUrl + " --path " + resPath + " run");
                String[] oneforallCmd = new String[]{"python", oneforallPathFull, "--target", oneforallTargetUrl, "--path", resPath, "run"};
                execAndFresh(oneforallCmd);
            } else if (StringUtil.notEmpty(oneforallChooseDomainDir)) {
                outputTextArea.setText(null);
                outputTextArea.append("\n");
                outputTextArea.append("开始执行OneForAll，命令为 --> python " + oneforallPathFull + " --targets " + oneforallChooseDomainDir + " --path " + resPath + " run");
                String[] oneforallCmd = new String[]{"python", oneforallPathFull, "--targets", oneforallChooseDomainDir, "--path", resPath,  "run"};
                execAndFresh(oneforallCmd);
                outputTextArea.append("\n");
            } else {
                outputTextArea.append("please choose single domain or domain dir for target");
            }
        });

//      打开OneForAll配置文件夹 --> 调用系统命令打开OneForAll的配置文件夹
        openOneforallConfigDirButton.addActionListener(e -> {
            try {
                if (OsUtil.isWindows()) {
                    Runtime.getRuntime().exec("explorer " + DirUtil.dirStructure(new String[]{oneforallPathFull.substring(0,oneforallPathFull.lastIndexOf("\\")), "config"}));
                } else {
                    Runtime.getRuntime().exec("open " + DirUtil.dirStructure(new String[]{oneforallPathFull.substring(0,oneforallPathFull.lastIndexOf("/")), "config"}));
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

//  继续断点任务，指定一个已存在的目标文件夹，以达到继续操作该任务的目的，底层原理就是全局将resPath设置为指定值
    public void initCountinueMission() {
        targetConfirmButton.addActionListener(e ->  {
            if (StringUtil.notEmpty(chooseExsitTextFiled.getText())) {
                startName = chooseExsitTextFiled.getText();
                startPath = new String(DirUtil.dirStructure(new String[]{currentPath, "res", startName}));
                resPath = startPath;
                outputFileConpanyName = startName;
                outputTextArea.append("你以开始断点任务，任务名称为 --->  " + startName + "\n断点任务结果路径为 ---> " + startPath);
            } else {
                JOptionPane.showMessageDialog(RuiScan, "请先指定一个要继续操作的公司名称，名字与结果文件夹中的以公司名字为名称的文件夹一致");
            }
        });
    }

//  一键无差别日站，暂时的功能是将上面聚合的Subdomain直接导入xray进行探测
    public void initOneKeyForAll() {
        onekeyAutoButton.addActionListener(e -> {
            if (StringUtil.notEmpty(outputFileConpanyName)) {
//            手动将任务以多线程的方式加入xray扫描，之后可以做一个限制  例如每次加10个线程进去执行，结束后再创建10个
//            FileInputStream inputStream = null;
//            try {
//                inputStream = new FileInputStream(domainFilePath);
//                  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String str = null;
//                while ((str = bufferedReader.readLine()) != null) {
//                    System.out.println(str);
//                    String[] xrayCmd = new String[]{xrayPathFull, "webscan", "--url", str, "--html-output", resultPath + "/" + str.replace(".", "_") + ".html"};
//                }
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
                if (xrayRadioButton.isSelected()) {
                    outputTextArea.append("\n");
                    outputTextArea.append("当前选择的扫描工具是xray");
                    String[] xrayCmd = new String[]{xrayPathFull, "webscan", "--url-file", domainFilePath, "--html-output", DirUtil.dirStructure(new String[]{resPath, "xray_" + outputFileConpanyName + ".html"})};
                    execAndFresh(xrayCmd);
                } else if (fscanRadioButton.isSelected()) {
                    outputTextArea.append("\n");
                    outputTextArea.append("当前选择的扫描工具是fscan");
                    String[] fscanCmd = new String[]{fscanPathFull, "-hf", ipFilePath, "-o", DirUtil.dirStructure(new String[]{resPath, "fscan_" + outputFileConpanyName + ".txt"})};
                    execAndFresh(fscanCmd);
                }
            } else {
                JOptionPane.showMessageDialog(RuiScan, "请先指定公司名运行ENScan然后提取域名然后再点击这里");
            }
        });
    }

//  在outputTextArea区域输出xray内置的POC
    public void initLookAllPoc() {
        showAllPocButton.addActionListener(e ->  {
            String[] xrayCmd = new String[]{xrayPathFull, "ws", "--list"};
            execAndFresh(xrayCmd);
        });
    }

//  一键聚合结果，将OneForAll的SubDomain和ENScan提取的Subdomain进行聚合
    public void initPolymerization() {
        startFingerButton.addActionListener(e -> {
            String missionDir = resPath;
            if (!StringUtil.notEmpty(outputFileConpanyName)) {
                missionDir = new String(DirUtil.dirStructure(new String[]{resPath, chooseExsitTextFiled.getText()}));
            }
            String allSubDomainTxt = DirUtil.findAllSubDomainTxt(missionDir);
            String allSubDomainCsv = allSubDomainTxt.replace(".txt", ".csv");
            try {
                ArrayList<String> domainList = StringUtil.domainControl(allSubDomainTxt);
                List ipList = StringUtil.readcsv(allSubDomainCsv);
                ipList = StringUtil.removeDuplicationByHashSet(ipList);
                ipList.remove("ip");
                if (!StringUtil.notEmpty(domainFilePath)) {
                    domainFilePath = DirUtil.dirStructure(new String[]{currentPath, "res", chooseExsitTextFiled.getText(), "domain_" + chooseExsitTextFiled.getText() + ".txt"});
                    ipFilePath = DirUtil.dirStructure(new String[]{currentPath, "res", chooseExsitTextFiled.getText(), "ip_" + chooseExsitTextFiled.getText() + ".txt"});
                }
                System.out.println(domainFilePath);
                System.out.println(ipFilePath);
                StringUtil.write2txt(domainList, domainFilePath);
                StringUtil.write2txt((ArrayList<String>) ipList, ipFilePath);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public MainForm() {
        init();
        initLoadTools();
        initToolPathChoose();
        initFofaForm();
        initExecEnscan();
        initExecJsfinder();
        initExecCipr();
        initExecOneforall();
        initOneKeyForAll();
        initLookAllPoc();
        initPolymerization();
        initCountinueMission();
    }

    public static void startMainForm() {
        JFrame frame = new JFrame("RuiScan");
        instance = new MainForm();
        frame.setContentPane(instance.RuiScan);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1280,700);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        final int WIDTH = 1280;
        final int HEIGHT = 700;
        int x=(width -WIDTH)/2;
        int y=(height - HEIGHT)/2;
        frame.setLocation(x,y);
    }
}
