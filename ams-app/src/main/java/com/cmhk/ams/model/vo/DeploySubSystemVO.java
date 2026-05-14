package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeploySubSystemVO {
    private Long id;
    private String deploySysNo;
    private String subSysNo;
    private String deploySysNameCn;
    private String deploySysDesc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
