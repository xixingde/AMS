package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class DeploySubSystemDTO {
    private Long id;
    private String deploySysNo;
    private String subSysNo;
    private String deploySysNameCn;
    private String deploySysDesc;
}
