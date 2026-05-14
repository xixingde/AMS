package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class TechStackDTO {
    private Long id;
    private String stackNo;
    private String category;
    private String name;
    private String selectionAdvice;
    private String responsibleLine;
}
