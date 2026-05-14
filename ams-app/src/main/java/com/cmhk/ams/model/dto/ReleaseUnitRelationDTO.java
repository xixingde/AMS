package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class ReleaseUnitRelationDTO {
    private Long id;
    private String deploySysNo;
    private String callerUnitNo;
    private String calleeUnitNo;
    private String protocol;
}
