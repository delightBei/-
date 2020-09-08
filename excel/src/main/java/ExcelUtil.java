import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表格处理
 *
 * @author  beiyuchao@aistrong.com
 * @Date 2020/9/8  11:22 上午
 */
public class ExcelUtil {

    public static String getCellValueByCell(Cell cell,String key,List<String> list){
        /**
         *
         *
         * @Author beiyuchao@aistrong.com
         * @Date 2020/9/8 11:02 上午
         * @return cellValue
         * @Param cell
         * @Param key
         * @Param list
        */
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        if(list.contains(key)){
            String stringCellValue = cell.getStringCellValue();
            double v = Double.parseDouble(stringCellValue);
            Date c= HSSFDateUtil.getJavaDate(v);
            cellValue = new SimpleDateFormat("yyyy/MM/dd").format(c);
            System.out.println(cellValue);
        }else {
            cellValue=cell.getStringCellValue();


        }
//        int cellType = cell.getCellType();
//        switch (cellType){
//            case Cell.CELL_TYPE_STRING: //字符串类型
//            cellValue = cell.getStringCellValue().trim();
//            break;
//            case Cell.CELL_TYPE_BOOLEAN: //布尔类型
//                cellValue = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_NUMERIC: //数值类型
//                if (Double.toString(cell.getNumericCellValue()).length() == 7) {
//                    cellValue = new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
//                }
//                else if (cell.getCellStyle().getDataFormatString().indexOf("%") == -1) {
//                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
//                 } else {
//                    cellValue = String.valueOf(cell.getNumericCellValue() *100);
//                    cellValue = cellValue.endsWith(".0") ? cellValue.replace(".0","") + "%" : cellValue + "%";
//            }
//            break;
//            default: //其他类型
//                cellValue = "";
//                break;
//        }
        return cellValue;
    }

    /**
     * 读取excel表格中特定的列
     *
     * @param sheetAPath
     *            文件
     * @throws
     */

    public static List<Map<String,String>> readExcel(File sheetAPath )  {
        /**
         * 读取表格
         *
         * @Author beiyuchao@aistrong.com
         * @Date 2020/9/8 11:01 上午
         * @return excelDate
         * @Param sheetAPath
        */
        FileInputStream fis = null;
        Workbook wb = null;
        List<String> list =null;
        try {
            list = Files.readAllLines(Paths.get("/Users/delight/工作文档/execl测试日期.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis = new FileInputStream(sheetAPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Map<String,String>> excelDate = new ArrayList<Map<String, String>>();

        try {
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum()+ 1; //表格行数
        for (int x = 1; x<sheet.getRow(1).getLastCellNum() ; x++) {
            Map<String,String> map = new HashMap<String, String>();
            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                if(row.getCell(x)!=null){
                    row.getCell(x).setCellType(Cell.CELL_TYPE_STRING);
                }
                String cellKey = row.getCell(0).getStringCellValue();
                Cell cell = row.getCell(x);
                if(cell==null) {
                    map.put(cellKey,"");
                    continue;
                }
                String cellValue = getCellValueByCell( cell,  row.getCell(0).getStringCellValue(),list);
                map.put(cellKey, cellValue);
            }
            excelDate.add(map);
        }
        for (int i = 0; i < excelDate.size(); i++) {
//            System.out.println(excelDate.get(i));
        }
//        System.out.println("通过Map.keySet遍历key和value：");
//        for (String key : map.keySet()) {
//            System.out.println("key= " + key + " and value= " + map.get(key));
//        }
        return excelDate;
    }

}
