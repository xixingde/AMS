package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class SubSystemDTO {
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
