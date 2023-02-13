
import com.reve2se.ruiscan.utils.OsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * 截取IP地址和端口号
 * 作者： 彭赛赛
 * 创建时间：2020/1/17 13:33
 */
public class read {
    public static void main(String[] args) {
        Pattern p = compile("(\\*\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
        Matcher m = p.matcher("http://192.168.60.333:2222/eduattendance");
        while (m.find()) {

            System.out.println("ip:" + m.group(1));
            System.out.println("port:" + m.group(2));
        }
        ArrayList<String> files = new ArrayList<String>();
        String path = new String(".");
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile()) {
                System.out.println(tempList[i]);
//            }
        }
    }

}
