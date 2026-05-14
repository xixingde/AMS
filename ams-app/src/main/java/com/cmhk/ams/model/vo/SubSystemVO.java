package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubSystemVO {
    private Long id;
    private String subSysNo;
    private String sysNo;
    private String subSysNameCn;
    private String subSysNameEn;
    private String devMode;
    private String subSysLevel;
    private String subSysStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
