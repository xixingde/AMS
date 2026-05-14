package com.cmhk.ams.controller;

import com.cmhk.ams.model.Result;
import com.cmhk.ams.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "逻辑部署架构图管理")
@RestController
@RequestMapping("/api/v1/diagrams")
@RequiredArgsConstructor
public class DiagramController {

    private final DiagramService diagramService;

    @Operation(summary = "获取架构图")
    @GetMapping("/{deploySysNo}")
    public Result<Map<String, Object>> getDiagram(@PathVariable String deploySysNo) {
        return Result.success(diagramService.getDiagram(deploySysNo));
    }

    @Operation(summary = "保存手动调整")
    @PostMapping("/{deploySysNo}/layout")
    public Result<Void> saveLayout(@PathVariable String deploySysNo, @RequestBody Map<String, String> body) {
        diagramService.saveLayout(deploySysNo, body.get("layoutData"), body.get("remark"));
        return Result.success();
    }

    @Operation(summary = "获取手动调整")
    @GetMapping("/{deploySysNo}/layout")
    public Result<Map<String, Object>> getLayout(@PathVariable String deploySysNo) {
        return Result.success(diagramService.getLayout(deploySysNo));
    }
}
