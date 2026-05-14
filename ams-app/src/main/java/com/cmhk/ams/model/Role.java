package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("roles")
public class Role {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String roleName;
    private String roleCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
