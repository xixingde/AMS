package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("deploy_sub_systems")
public class DeploySubSystem extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String deploySysNo;
    private String subSysNo;
    private String deploySysNameCn;
    private String deploySysDesc;
}
