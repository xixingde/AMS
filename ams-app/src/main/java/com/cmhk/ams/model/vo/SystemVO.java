package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemVO {
    private Long id;
    private String sysNo;
    private String sysNameCn;
    private String sysNameEn;
    private String sysAlias;
    private String sysType;
    private String sysOwner;
    private String sysLevel;
    private String protectionLevel;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
