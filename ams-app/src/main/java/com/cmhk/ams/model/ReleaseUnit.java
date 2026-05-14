package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("release_units")
public class ReleaseUnit extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String unitNo;
    private String deploySysNo;
    private String unitNameCn;
    private String unitType;
    private String networkZone;
    private String deployMode;
}
