package com.reve2se.ruiscan.utils;

import java.io.*;
import java.util.ArrayList;


public class StringUtil {
        public static boolean notEmpty(String str) {
            return str != null && !str.equals("");
        }

        public static ArrayList<String> domainControl(String path) throws IOException {
            ArrayList<String> domainList = new ArrayList<String>();
            File file=new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null) {
                if (!lineTxt.contains("www")) {
                    domainList.add(lineTxt);
                    System.out.println(lineTxt);
                }
            }
            read.close();
            return domainList;
        }

        public static void write2txt(ArrayList<String> domainList, String path) throws IOException {
            File domain_file = new File(path);
            FileWriter domain_fw = new FileWriter(domain_file, true);
            BufferedWriter domain_bw = new BufferedWriter(domain_fw);
            for (int i = 0; i < domainList.size(); i++) {
                domain_bw.newLine();
                domain_bw.write(domainList.get(i));
                domain_bw.flush();
            }
        }

        public static void autoReplace(String filePath, String oldstr, String newStr) {
            //创建文件
            File file = new File(filePath);
            //记录文件长度
            Long fileLength = file.length();
            //记录读出来的文件的内容
            byte[] fileContext = new byte[fileLength.intValue()];
            FileInputStream in = null;
            PrintWriter out = null;
            try {
                in = new FileInputStream(filePath);
                //读出文件全部内容(内容和文件中的格式一致,含换行)
                in.read(fileContext);
                // 避免出现中文乱码
                String str = new String(fileContext, "utf-8");
                //修改对应字符串内容
                str = str.replace(oldstr, newStr);
                //再把新的内容写入文件
                out = new PrintWriter(filePath);
                out.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}