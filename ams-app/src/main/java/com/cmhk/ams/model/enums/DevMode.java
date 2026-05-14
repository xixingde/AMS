package com.cmhk.ams.model.enums;

public enum DevMode {
    AGILE("敏捷"),
    WATERFALL("瀑布"),
    HYBRID("混合");

    private final String label;

    DevMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
