package com.cmhk.ams.controller;

import com.cmhk.ams.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Tag(name = "数据字典")
@RestController
@RequestMapping("/api/v1/dictionaries")
public class DictionaryController {

    private static final Map<String, List<Map<String, String>>> DICT = new HashMap<>();

    static {
        DICT.put("system_type", Arrays.asList(
            Map.of("value", "COMPREHENSIVE_OFFICE", "label", "综合办公"),
            Map.of("value", "BUSINESS_MANAGEMENT", "label", "经营管理"),
            Map.of("value", "PRODUCTION_OPERATION", "label", "生产运营")
        ));
        DICT.put("sys_level", Arrays.asList(
            Map.of("value", "CORE", "label", "核心"),
            Map.of("value", "IMPORTANT", "label", "重要"),
            Map.of("value", "GENERAL", "label", "一般")
        ));
        DICT.put("protection_level", Arrays.asList(
            Map.of("value", "LEVEL_1", "label", "一级"),
            Map.of("value", "LEVEL_2", "label", "二级"),
            Map.of("value", "LEVEL_3", "label", "三级"),
            Map.of("value", "LEVEL_4", "label", "四级"),
            Map.of("value", "LEVEL_5", "label", "五级")
        ));
        DICT.put("sub_sys_status", Arrays.asList(
            Map.of("value", "ONLINE", "label", "已上线"),
            Map.of("value", "DEVELOPING", "label", "研发中"),
            Map.of("value", "PLANNING", "label", "规划中"),
            Map.of("value", "OFFLINE", "label", "已下线")
        ));
        DICT.put("dev_mode", Arrays.asList(
            Map.of("value", "AGILE", "label", "敏捷"),
            Map.of("value", "WATERFALL", "label", "瀑布"),
            Map.of("value", "HYBRID", "label", "混合")
        ));
        DICT.put("selection_advice", Arrays.asList(
            Map.of("value", "RECOMMENDED", "label", "推荐"),
            Map.of("value", "CONDITIONAL", "label", "有条件推荐"),
            Map.of("value", "OBSOLETE", "label", "淘汰"),
            Map.of("value", "EVALUATING", "label", "评估中")
        ));
        DICT.put("responsible_line", Arrays.asList(
            Map.of("value", "APPLICATION", "label", "应用线"),
            Map.of("value", "TECH_PLATFORM", "label", "技术平台线"),
            Map.of("value", "DATA", "label", "数据线"),
            Map.of("value", "INFRASTRUCTURE", "label", "基础设施线")
        ));
        DICT.put("network_zone", Arrays.asList(
            Map.of("value", "INTERNET", "label", "互联网区"),
            Map.of("value", "DMZ", "label", "DMZ区"),
            Map.of("value", "OFFICE_NETWORK", "label", "办公网区"),
            Map.of("value", "CORE", "label", "核心区"),
            Map.of("value", "MANAGEMENT", "label", "管理区")
        ));
    }

    @Operation(summary = "查询字典项")
    @GetMapping("/{category}")
    public Result<List<Map<String, String>>> getByCategory(@PathVariable String category) {
        return Result.success(DICT.getOrDefault(category, Collections.emptyList()));
    }
}
