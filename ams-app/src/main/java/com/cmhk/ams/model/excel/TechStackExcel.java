package com.cmhk.ams.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TechStackExcel {
    @ExcelProperty("技术栈编号")
    private String stackNo;
    @ExcelProperty("分类")
    private String category;
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("选型建议")
    private String selectionAdvice;
    @ExcelProperty("负责条线")
    private String responsibleLine;
}
