package com.cmhk.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("systems")
public class System extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String sysMasterNo;
    private String sysNo;
    private String sysNameCn;
    private String sysNameEn;
    private String sysAlias;
    private String sysType;
    private String sysOwner;
    private String sysDesc;
    private Integer splitFromOther;
    private String dataSecurityLevel;
    private Integer sensitiveDataInvolved;
    private String sensitiveDataLabel;
    private String sysLevel;
    private String protectionLevel;
    private String applyReason;
}
