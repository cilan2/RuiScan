package com.reve2se.ruiscan.utils;

import com.reve2se.ruiscan.form.MainForm;

public class ExecUtil {
    public static void chmod(String path) {
        String[] chmodCmd = new String[]{"/bin/chmod", "u+x", path};
        exec(chmodCmd);

    }

    public static void execCmdNoRet(String[] cmd) {
        System.out.println("exec execCmdNoRet");
        System.out.println("execCmdNoRet command is " + cmd);
        exec(cmd);
    }

    public static Process exec(String[] cmdArray) {
        try {
            String cmd = String.join(" ", cmdArray);
            System.out.println("当前执行的命令为 ---> " + cmd);
            return new ProcessBuilder(cmdArray).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    public static Process pythonExec(String[] cmdArray) {
//        try {
//            String cmd =
//        }
//    }

    public static void execOpen(String outputFilePath) {
        if (OsUtil.isWindows()) {
            String cmd = String.format("start %s", outputFilePath);
            String[] ruiCmd = new String[]{"cmd.exe", "/c", String.format("%s", cmd)};
            exec(ruiCmd);
        } else {
            String cmd = String.format("open %s", outputFilePath);
            String[] ruiCmd = new String[]{"/bin/bash", "-c", String.format("%s", cmd)};
            exec(ruiCmd);
        }
    }

//    public static void pythonExecOpen(String outputFilePath) {
//        if (OsUtil.isWindows()) {
//            String cmd = String.format("start %s", outputFilePath);
//            String[] ruiCmd = new String[]{"cmd.exe", "/c", "python", String.format("%s", cmd)}
//        }
//    }
}
