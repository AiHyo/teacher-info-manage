package com.aih.utils.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class MyExcelUtil {

        /**
         * 自适应宽度(中文支持)
         *
         * @param sheet sheet
         * @param size  因为for循环从0开始，size值为 列数-1
         */
        public static void setSizeColumn(Sheet sheet, int size) {
            for (int columnNum = 0; columnNum <= size; columnNum++) {
                int columnWidth = sheet.getColumnWidth(columnNum) / 256;
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row currentRow;
                    //当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }

                    if (currentRow.getCell(columnNum) != null) {
                        Cell currentCell = currentRow.getCell(columnNum);
                        if (currentCell.getCellType() == CellType.STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                sheet.setColumnWidth(columnNum, columnWidth * 256);
            }
        }

/*
    public static List<Dict> exportData(Map<String,String>map,
                                        List<String> fieldList,
                                        List<?> list,
                                        Class<?> clazz){
        try {
            List<Dict> res = new ArrayList<>();
            //遍历需要导出的集合
            for (Object o : list) {
                Dict data = new Dict();
                //获取map中需要匹配的字段
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String,String> entry : entries) {
                    if (fieldList != null && fieldList.size() > 0) {
                        if (fieldList.contains(entry.getKey())) {
                            Field field = clazz.getDeclaredField(entry.getKey());
                            data.set(entry.getValue(),field.get(o));
                        }
                    }else {
                        Field field = clazz.getDeclaredField(entry.getKey());
                        data.set(entry.getValue(),field.get(o));
                    }
                }
                res.add(data);
            }
            return res;
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCodeMsg.DATA_EXPORT_ERROR);
        }
    }*/

/*    public static void excelExport(List<Map<String,Object>> dataList,
                                   HttpServletResponse response,
                                   String fileName) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        ServletOutputStream outputStream = null;
        try {
            writer.write(dataList,true); //标题行true
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");//解决前端获取不到文件名问题
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);//之后会自动关闭流
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(writer);
        }*/


    }

