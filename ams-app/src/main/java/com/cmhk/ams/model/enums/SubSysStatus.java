package com.cmhk.ams.model.enums;

public enum SubSysStatus {
    ONLINE("已上线"),
    DEVELOPING("研发中"),
    PLANNING("规划中"),
    OFFLINE("已下线");

    private final String label;

    SubSysStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
