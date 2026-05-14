package com.cmhk.ams.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ReleaseUnitExcel {
    @ExcelProperty("发布单元编号")
    private String unitNo;
    @ExcelProperty("所属部署子系统编号")
    private String deploySysNo;
    @ExcelProperty("中文名称")
    private String unitNameCn;
    @ExcelProperty("类型")
    private String unitType;
    @ExcelProperty("网络区域")
    private String networkZone;
    @ExcelProperty("部署方式")
    private String deployMode;
}
