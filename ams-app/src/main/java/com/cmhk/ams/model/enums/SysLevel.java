package com.cmhk.ams.model.enums;

public enum SysLevel {
    CORE("核心"),
    IMPORTANT("重要"),
    GENERAL("一般");

    private final String label;

    SysLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
