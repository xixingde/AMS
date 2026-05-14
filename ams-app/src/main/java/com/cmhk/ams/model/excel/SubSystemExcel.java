package com.cmhk.ams.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubSystemExcel {
    @ExcelProperty("子系统编号")
    private String subSysNo;
    @ExcelProperty("所属系统编号")
    private String sysNo;
    @ExcelProperty("中文名称")
    private String subSysNameCn;
    @ExcelProperty("英文名称")
    private String subSysNameEn;
    @ExcelProperty("开发模式")
    private String devMode;
    @ExcelProperty("子系统级别")
    private String subSysLevel;
    @ExcelProperty("等保级别")
    private String subSysProtectionLevel;
    @ExcelProperty("状态")
    private String subSysStatus;
}
