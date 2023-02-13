import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;

public class ReadExcel {
    public static void main(String[] args) {
        //excel文件路径
        String excelPath = "/Users/lichengxuan/IdeaProjects/RuiScan/res/南瑞集团有限公司/2023-01-17-南瑞集团有限公司.xlsx";

        try {
            File directory = new File("");
            File domain_file = new File(directory.getAbsolutePath() + "/res/domain_txt.txt");
            FileWriter domain_fw = new FileWriter(domain_file);
            BufferedWriter domain_bw = new BufferedWriter(domain_fw);
            File ip_file = new File(directory.getAbsolutePath() + "/res/ip_txt.txt");
            FileWriter ip_fw = new FileWriter(ip_file);
            BufferedWriter ip_bw = new BufferedWriter(ip_fw);
            String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            Pattern pattern = Pattern.compile(ip);
            //String encoding = "GBK";
            File excel = new File(excelPath);

            if (excel.isFile() && excel.exists()) { //判断文件是否存在

                String[] split = excel.getName().split("\\."); //.是特殊字符，需要转义！！！！！
                Workbook wb;

                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel); //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");

                    return;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0); //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1; //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex;
                     rIndex++) { //遍历行
                    Row row = sheet.getRow(rIndex);

                    if (row != null) {
//                        int firstCellIndex = row.getFirstCellNum();
//                        int lastCellIndex = row.getLastCellNum();

//                        for (int cIndex = firstCellIndex;
//                             cIndex < lastCellIndex; cIndex++) { //遍历列

                            Cell cell = row.getCell(0);

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
//                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//public class ReadExcel {
////    InputStream iStream, int columnIndex
//
//
//    public static List<String> getColumn(InputStream iStream, int columnIndex) throws IOException, InvalidFormatException {
//        List<String> columnList=new ArrayList<>();
//        Workbook book = WorkbookFactory.create(iStream);
//        Sheet sheet = book.getSheetAt(0);
//
//        for (int runNum =0; runNum <=sheet.getLastRowNum();runNum++) {
//            Row row = sheet.getRow(runNum);
//            if (row != null) {
//                int minColIx = row.getFirstCellNum();
//                int maxColIx = row.getLastCellNum();
//                //遍历该行，获取每个cell元素
//                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
//                    Cell cell = row.getCell(colIx);
//                    //获取指定的一列
//                    if (cell.getColumnIndex() == columnIndex) {
//                        columnList.add(cell.getStringCellValue());
//                        System.out.println(cell.getStringCellValue());
//                    } else {
//                        break;
//                    }
//                }
//            }
//        }
//        return columnList;
//    }
//
//    public static void main(String[] args) throws IOException, InvalidFormatException {
//        getColumn(new FileInputStream(new File("/Users/lichengxuan/Desktop/ruiscan/tools/ENScan/res/2023-01-12-南瑞集团有限公司.xlsx")), 1);
//    }
//}
//
