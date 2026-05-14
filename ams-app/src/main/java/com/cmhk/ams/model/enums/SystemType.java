package com.cmhk.ams.model.enums;

public enum SystemType {
    COMPREHENSIVE_OFFICE("综合办公"),
    BUSINESS_MANAGEMENT("经营管理"),
    PRODUCTION_OPERATION("生产运营");

    private final String label;

    SystemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
