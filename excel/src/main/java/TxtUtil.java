import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * txt文本处理
 *
 * @author  beiyuchao@aistrong.com
 * @Date 2020/9/8  11:22 上午
 */
public class TxtUtil {
    /**
     * 读取目录下所有txt文件
     *
     * @Author beiyuchao@aistrong.com
     * @Date 2020/9/8 11:18 上午
     * @return
     * @param path
     * @param fileNameList
     */
    public static ArrayList<String> readFiles1(String path, ArrayList<String> fileNameList) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    readFiles1(files[i].getPath(), fileNameList);
                } else {

                    String path1 = files[i].getPath();
                    boolean status = path1.contains(".txt");
//                    String fileName = path1.substring(path1.lastIndexOf("\\") + 1);
                    if (status)
                    fileNameList.add(path1);
                }
            }
        } else {
            String path1 = file.getPath();
            String fileName = path1.substring(path1.lastIndexOf("\\") + 1);
            fileNameList.add(fileName);
        }
        return fileNameList;
    }
    /**
     * 读取txt文本内容
     *
     * @Author beiyuchao@aistrong.com
     * @Date 2020/9/8 11:15 上午
     * @return map
     * @param file
     */
    public Map<String,String> readList (File file) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        try {
            String str = "";
            String str1 = "";
            fis = new FileInputStream(file);// FileInputStream
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            while ((str = br.readLine()) != null) {
                String[] s=str.split("=");//切割
                if (s.length == 1){
                    map.put(s[0],"");
                    continue;
                }
                String ss=s[1];
                Pattern compile = Pattern.compile("((\\d{1,2})\\.?(\\d{0,4})%)");
                Matcher matcher = compile.matcher(ss);
                while(matcher.find()){
                    String group = matcher.group(1);
                    String group1 = matcher.group(2);
                    if(group1.length()==1){
                        group1="0"+group1;
                    }
                    ss=ss.replace(matcher.group(1),"0."+group1+matcher.group(3));
                    if(ss.indexOf(".") > 0){
                        ss = ss.replaceAll("0+?$", "");//去掉多余的0
                        ss = ss.replaceAll("[.]$", "");//如最后一位是.则去掉
                    }
                }
                map.put(s[0],ss);
            }
            // 当读取的一行不为空时,把读到的str的值赋给str1
        } catch (Exception e) {
            e.printStackTrace();
        }
            System.out.println(map);
        return map;
    }
}
