package com.cmhk.ams.model.enums;

public enum NetworkZone {
    INTERNET("互联网区"),
    DMZ("DMZ区"),
    OFFICE_NETWORK("办公网区"),
    CORE("核心区"),
    MANAGEMENT("管理区");

    private final String label;

    NetworkZone(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
