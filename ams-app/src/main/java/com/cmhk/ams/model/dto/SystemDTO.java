package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class SystemDTO {
    private Long id;
    private String sysMasterNo;
    private String sysNo;
    private String sysNameCn;
    private String sysNameEn;
    private String sysAlias;
    private String sysType;
    private String sysOwner;
    private String sysDesc;
    private Integer splitFromOther;
    private String dataSecurityLevel;
    private Integer sensitiveDataInvolved;
    private String sensitiveDataLabel;
    private String sysLevel;
    private String protectionLevel;
    private String applyReason;
}
