package com.cmhk.ams.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReleaseUnitDTO {
    private Long id;
    private String unitNo;
    private String deploySysNo;
    private String unitNameCn;
    private String unitType;
    private String networkZone;
    private String deployMode;
    private List<String> stackNos;
}
