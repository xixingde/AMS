package com.cmhk.ams.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

public class ExcelUtil {

    public static <T> void export(HttpServletResponse response, String fileName, List<T> data, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("Sheet1")
                .doWrite(data);
    }

    public static <T> ExcelImportListener<T> read(InputStream inputStream, Class<T> clazz, Consumer<List<T>> dataConsumer) {
        ExcelImportListener<T> listener = new ExcelImportListener<>(dataConsumer);
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();
        return listener;
    }

    public static <T> void template(HttpServletResponse response, String fileName, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + "_template.xlsx");
        EasyExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("Sheet1")
                .doWrite(List.of());
    }
}
