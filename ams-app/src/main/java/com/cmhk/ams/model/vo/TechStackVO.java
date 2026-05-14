package com.cmhk.ams.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TechStackVO {
    private Long id;
    private String stackNo;
    private String category;
    private String name;
    private String selectionAdvice;
    private String responsibleLine;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
