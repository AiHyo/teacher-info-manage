package com.aih.utils.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyExcelUtil {

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

    public static void excelExport(List<Map<String,Object>> dataList,
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
        }


    }

}
