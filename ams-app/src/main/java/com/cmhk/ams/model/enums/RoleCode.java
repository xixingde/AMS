package com.cmhk.ams.model.enums;

public enum RoleCode {
    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    ADMIN("ADMIN", "管理员"),
    USER("USER", "普通用户");

    private final String code;
    private final String label;

    RoleCode(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
