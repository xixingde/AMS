package com.cmhk.ams.model.enums;

public enum ResponsibleLine {
    APPLICATION("应用线"),
    TECH_PLATFORM("技术平台线"),
    DATA("数据线"),
    INFRASTRUCTURE("基础设施线");

    private final String label;

    ResponsibleLine(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
