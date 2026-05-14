package com.cmhk.ams.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DeploySubSystemExcel {
    @ExcelProperty("部署子系统编号")
    private String deploySysNo;
    @ExcelProperty("所属子系统编号")
    private String subSysNo;
    @ExcelProperty("中文名称")
    private String deploySysNameCn;
    @ExcelProperty("描述")
    private String deploySysDesc;
}
