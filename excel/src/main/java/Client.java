import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * txt文本处理
 *
 * @author  beiyuchao@aistrong.com
 * @Date 2020/9/8  11:22 上午
 */
public class Client {

    public static void write(List<Map<String, String>> excelData, List<Map<String,String>> txtRead){
        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("识别结果");
        // 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
        // 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //计算格式
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后5位
        numberFormat.setMaximumFractionDigits(5);
        //每行准确率的全局变量
        Map<String, Integer> rowAcc = new HashMap<String, Integer>();


        for (int i = 0; i < txtRead.size(); i++) {
            Map<String,String> map1 = null;
            Map<String,String> map = null;
            map = txtRead.get(i);

            String standValue = map.get("理财产品代码");
            for(Map<String,String> map2:excelData){
                String value = map2.get("理财产品代码");
                if(standValue.equals(value)){
                    map1=map2;
                    break;
                }
            }

            //控制台输出验证
            for(String key1 : map1.keySet()){
                String map1value = map1.get(key1);
                String mapvalue = map.get(key1);

                if (map1value.equals(mapvalue)){
//                    System.out.println("正确");
                }
                if (!map1value.equals(mapvalue)) {
                    System.out.println(key1+"   测试数据  ="+mapvalue+"   答案  = "+map1value);
                }
            }

            // 添加表头内容
            HSSFCell headCell = hssfRow.createCell(0);
            headCell.setCellValue("字段");
            headCell.setCellStyle(cellStyle);

            headCell = hssfRow.createCell(1+2*i);
            headCell.setCellValue("监测数据");
            headCell.setCellStyle(cellStyle);

            headCell = hssfRow.createCell(2+2*i);
            headCell.setCellValue("答案");
            headCell.setCellStyle(cellStyle);


            // 添加数据内容
            int j=1;//循环次数
            int t=0;//每篇准确率
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(cellStyle.SOLID_FOREGROUND);
            //输入表格数据循环
            for (String key1 : map1.keySet()) {
                HSSFRow hssfRow2;
                //控制第一次循环时创建行，其余情况获取行
                if(i==0){
                    hssfRow2 = sheet.createRow(j++);
                }else {
                    hssfRow2 = sheet.getRow(j++);
                }
                String map1Value = map1.get(key1);
                String mapValue = map.get(key1);
                HSSFCellStyle tempStyle= null;
                if (!rowAcc.containsKey(key1)){
                    rowAcc.put(key1,0);
                }
                //比对数据
                if (!map1Value.equals(mapValue)) {
                    tempStyle=style;
                }else {
                    tempStyle=cellStyle;
                    t=t+1;
                    rowAcc.put(key1,rowAcc.get(key1)+1);
                }
                // 创建单元格，并设置值
                HSSFCell cell = hssfRow2.createCell(0);
                cell.setCellValue(key1);
                // 写入比对文本
                cell = hssfRow2.createCell(1+2*i);
                cell.setCellValue(mapValue);
                cell.setCellStyle(tempStyle);
                // 写入比对答案
                cell = hssfRow2.createCell(2+2*i);
                cell.setCellValue(map1Value);
                cell.setCellStyle(tempStyle);
            }

            String result = numberFormat.format((float)t/(float)j*100);
            System.out.println(result);
            //控制第一次循环时创建行，其余情况获取行
            HSSFRow resultRow;
            if(i==0){
                resultRow = sheet.createRow(j+1);;
            }else {
                resultRow = sheet.getRow(j+1);
            }
            HSSFCell resultName = resultRow.createCell(0);
            resultName.setCellValue("准确率");
            HSSFCell resultCell = resultRow.createCell(1+2*i);
            resultCell.setCellValue(result);
        }
        //写入比对准确率
        HSSFCell AccCell = hssfRow.createCell(3+2*txtRead.size());
        AccCell.setCellValue("每个字段准确率");
        AccCell.setCellStyle(cellStyle);
        int k =1;
        for (String key1 : rowAcc.keySet()){
            String rowResult = numberFormat.format((float)rowAcc.get(key1)/(float)txtRead.size()*100);
            HSSFRow AccResultRow=null ;
            AccResultRow = sheet.getRow(k++);
            HSSFCell AccResultCell = AccResultRow.createCell(txtRead.size()*2+2);
            AccResultCell.setCellValue(rowResult);
        }


        // 保存Excel文件
        try {
            OutputStream outputStream = new FileOutputStream("/Users/delight/工作文档/excel对比/test.xls");
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void  main(String[] args){

        // 表格导出
        ExcelUtil excel = new ExcelUtil();
        File sheetAPath = new File("/Users/delight/工作文档/excel对比/理财产品说明书(1).xlsx");
        List<Map<String, String>> excelData=null;
        try {
            System.out.println("正在读取表格...");

           excelData = ExcelUtil.readExcel(sheetAPath);
            //读取第二列
            System.out.println("读取完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //文档查找
        String filePath = "/Users/delight/工作文档/excel对比";
        List<Map<String,String>> txtRead = new ArrayList<Map<String, String>>();
        try {
            ArrayList<String> fileNameList = TxtUtil.readFiles1(filePath, new ArrayList<String>());
            System.out.println(fileNameList.size());
            // 对txt文件进行遍历
            for (int i = 0; i < fileNameList.size(); i++) {
                File file = new File(fileNameList.get(i));
                TxtUtil txt = new TxtUtil();
                Map<String,String> map = txt.readList(file);
                txtRead.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 调用write，输出Excel文件
        write(excelData,txtRead);

    }
}

