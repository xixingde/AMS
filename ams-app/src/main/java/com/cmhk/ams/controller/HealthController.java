package com.cmhk.ams.controller;

import com.cmhk.ams.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "健康检查")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(summary = "存活探针")
    @GetMapping("/live")
    public Result<String> live() {
        return Result.success("alive");
    }

    @Operation(summary = "就绪探针")
    @GetMapping("/ready")
    public Result<String> ready() {
        return Result.success("ready");
    }
}
