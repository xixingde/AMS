package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tech_stacks")
public class TechStack extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String stackNo;
    private String category;
    private String name;
    private String selectionAdvice;
    private String responsibleLine;
}
