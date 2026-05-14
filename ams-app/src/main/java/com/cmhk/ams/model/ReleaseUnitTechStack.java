package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("release_unit_tech_stack")
public class ReleaseUnitTechStack {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String unitNo;
    private String stackNo;
    private LocalDateTime createdAt;
}
