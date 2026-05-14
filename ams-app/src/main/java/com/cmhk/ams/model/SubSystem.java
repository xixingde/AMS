package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sub_systems")
public class SubSystem extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String subSysMasterNo;
    private String subSysNo;
    private String sysNo;
    private String subSysNameCn;
    private String subSysNameEn;
    private String devMode;
    private String subSysLevel;
    private String subSysProtectionLevel;
    private String subSysStatus;
    private String birthCertNo;
}
