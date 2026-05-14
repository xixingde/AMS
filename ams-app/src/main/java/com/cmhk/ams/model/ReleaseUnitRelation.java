package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("release_unit_relations")
public class ReleaseUnitRelation extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String deploySysNo;
    private String callerUnitNo;
    private String calleeUnitNo;
    private String protocol;
}
