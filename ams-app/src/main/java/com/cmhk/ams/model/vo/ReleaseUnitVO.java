package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReleaseUnitVO {
    private Long id;
    private String unitNo;
    private String deploySysNo;
    private String unitNameCn;
    private String unitType;
    private String networkZone;
    private String deployMode;
    private List<String> stackNos;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
