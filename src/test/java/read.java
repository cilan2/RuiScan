import java.io.BufferedReader;
import java.io.FileReader;

public class read {

    public static void main(String[] args) throws Exception {
        String splitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader("/Users/lichengxuan/IdeaProjects/RuiScan/res/南瑞集团有限公司/all_subdomain_result_20230206_141307.csv"));
        String line;
        while((line = br.readLine()) != null){
            String[] b = line.split(splitBy);
            System.out.println(b[8]);
        }
        br.close();

    }
}