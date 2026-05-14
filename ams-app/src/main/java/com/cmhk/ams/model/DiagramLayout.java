package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("diagram_layouts")
public class DiagramLayout {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String deploySysNo;
    private String layoutData;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
