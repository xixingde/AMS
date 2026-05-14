package com.cmhk.ams.model.enums;

public enum ProtectionLevel {
    LEVEL_1("一级"),
    LEVEL_2("二级"),
    LEVEL_3("三级"),
    LEVEL_4("四级"),
    LEVEL_5("五级");

    private final String label;

    ProtectionLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
