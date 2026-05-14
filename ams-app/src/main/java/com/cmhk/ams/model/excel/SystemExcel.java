package com.cmhk.ams.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SystemExcel {
    @ExcelProperty("系统编号")
    private String sysNo;
    @ExcelProperty("中文名称")
    private String sysNameCn;
    @ExcelProperty("英文名称")
    private String sysNameEn;
    @ExcelProperty("系统别名")
    private String sysAlias;
    @ExcelProperty("系统类型")
    private String sysType;
    @ExcelProperty("系统属主")
    private String sysOwner;
    @ExcelProperty("系统级别")
    private String sysLevel;
    @ExcelProperty("等保级别")
    private String protectionLevel;
}
