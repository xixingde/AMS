package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReleaseUnitRelationVO {
    private Long id;
    private String deploySysNo;
    private String callerUnitNo;
    private String calleeUnitNo;
    private String protocol;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
