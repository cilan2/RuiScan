package com.reve2se.ruiscan.model;
import com.reve2se.ruiscan.utils.DirUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.regex.Pattern;

public class Excel2Txt {
    public static void excel2txt(String excelPath, String company_name) {
        try {
            File directory = new File("");
            File domain_file = new File(DirUtil.dirStructure(new String[]{directory.getAbsolutePath(), "res", company_name, "domain_" + company_name + ".txt"}));
            FileWriter domain_fw = new FileWriter(domain_file);
            BufferedWriter domain_bw = new BufferedWriter(domain_fw);
            File ip_file = new File(DirUtil.dirStructure(new String[]{directory.getAbsolutePath(), "res", company_name, "ip_" + company_name + ".txt"}));
            FileWriter ip_fw = new FileWriter(ip_file);
            BufferedWriter ip_bw = new BufferedWriter(ip_fw);
            String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            Pattern pattern = Pattern.compile(ip);
            //String encoding = "GBK";
            File excel = new File(excelPath);

            if (excel.isFile() && excel.exists()) { //判断文件是否存在

                String[] split = excel.getName().split("\\."); //.是特殊字符，需要转义！！！！！

                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel); //文件流对象
                    HSSFWorkbook wb = new HSSFWorkbook(fis);

                    //开始解析
                    HSSFSheet sheet = wb.getSheetAt(0); //读取sheet 0
                    int firstRowIndex = sheet.getFirstRowNum() + 1; //第一行是列名，所以不读
                    int lastRowIndex = sheet.getLastRowNum();
                    for (int rIndex = firstRowIndex; rIndex <= lastRowIndex;
                         rIndex++) { //遍历行
                        HSSFRow row = sheet.getRow(rIndex);
                        if (row != null) {
                            HSSFCell cell = row.getCell(0);

                            if (cell != null) {
                                if (pattern.matcher(cell.toString()).matches()) {
                                    ip_bw.write(cell.toString());
                                    ip_bw.flush();
                                    ip_bw.newLine();
                                } else {
                                    domain_bw.write(cell.toString());
                                    domain_bw.flush();
                                    domain_bw.newLine();
                                }
                            }
                        }
                    }
                } else if ("xlsx".equals(split[1])) {
                    XSSFWorkbook wb = new XSSFWorkbook(excel);
                    //开始解析
                    XSSFSheet sheet = wb.getSheetAt(0); //读取sheet 0
                    int firstRowIndex = sheet.getFirstRowNum() + 1; //第一行是列名，所以不读
                    int lastRowIndex = sheet.getLastRowNum();
                    for (int rIndex = firstRowIndex; rIndex <= lastRowIndex;
                         rIndex++) { //遍历行
                        XSSFRow row = sheet.getRow(rIndex);
                        if (row != null) {
                            XSSFCell cell = row.getCell(0);

                            if (cell != null) {
                                if (pattern.matcher(cell.toString()).matches()) {
                                    ip_bw.write(cell.toString());
                                    ip_bw.flush();
                                    ip_bw.newLine();
                                } else {
                                    domain_bw.write(cell.toString());
                                    domain_bw.flush();
                                    domain_bw.newLine();
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("文件类型错误!");

                    return;
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
