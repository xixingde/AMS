package com.cmhk.ams.service;

import java.util.Map;

public interface DiagramService {
    Map<String, Object> getDiagram(String deploySysNo);
    void saveLayout(String deploySysNo, String layoutData, String remark);
    Map<String, Object> getLayout(String deploySysNo);
}
