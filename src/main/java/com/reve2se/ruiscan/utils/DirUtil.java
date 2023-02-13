package com.reve2se.ruiscan.utils;

import java.io.File;
import java.util.ArrayList;

//返回指定路径下的文件列表
public class DirUtil {
    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
                files.add(tempList[i].toString());
            }
        return files;
    }

//  判断指定文件是否存在
    public static boolean dirIsExist(String path) {
        File folder = new File(path);
        return folder.exists();
    }

//  根据给定的路径自动返回windows和Linux、mac的两种文件路径<主要就是处理|和\分割符的问题>
    public static String dirStructure(String[] args) {
        String path = args[0];
            String[] temp = new String[args.length - 1];
            System.arraycopy(args, 1, temp, 0, temp.length);
            if (OsUtil.isWindows()) {
                for (String arg : temp) {
                    path = path + "\\" + arg;
                }
                if (new File(path).isDirectory()) {
                    path = path + "\\";
                }
            } else {
                for (String arg : temp) {
                    path = path + "/" + arg;
                }
                if (new File(path).isDirectory()) {
                    path = path + "/";
                }
            }
        return path;
    }

//  返回OneForAll执行结果中的AllSubDomain文件的txt文件具体的路径
    public static String findAllSubDomainTxt(String resPath) {
        File file = new File(resPath);
        File[] tempList = file.listFiles();
        String allSubDomainTxtName = null;
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].toString().contains("all_subdomain_result_") && tempList[i].toString().contains("txt")) {
                allSubDomainTxtName = new String(tempList[i].toString());
            }
        }
        return allSubDomainTxtName;
    }
}
