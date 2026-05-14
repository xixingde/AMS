package com.cmhk.ams.model.enums;

public enum SelectionAdvice {
    RECOMMENDED("推荐"),
    CONDITIONAL("有条件推荐"),
    OBSOLETE("淘汰"),
    EVALUATING("评估中");

    private final String label;

    SelectionAdvice(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
