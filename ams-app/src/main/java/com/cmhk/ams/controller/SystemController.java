package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.SystemDTO;
import com.cmhk.ams.model.vo.SystemVO;
import com.cmhk.ams.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "系统清单管理")
@RestController
@RequestMapping("/api/v1/systems")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;

    @Operation(summary = "查询系统列表")
    @GetMapping
    public Result<Page<SystemVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sysNo,
            @RequestParam(required = false) String sysNameCn,
            @RequestParam(required = false) String sysType,
            @RequestParam(required = false) String sysOwner,
            @RequestParam(required = false) String sysLevel) {
        return Result.success(systemService.page(page, pageSize, sysNo, sysNameCn, sysType, sysOwner, sysLevel));
    }

    @Operation(summary = "查询系统详情")
    @GetMapping("/{id}")
    public Result<SystemVO> detail(@PathVariable Long id) {
        return Result.success(systemService.detail(id));
    }

    @Operation(summary = "创建系统")
    @PostMapping
    public Result<Void> create(@RequestBody SystemDTO dto) {
        systemService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新系统")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SystemDTO dto) {
        systemService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除系统")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        systemService.delete(id);
        return Result.success();
    }

    @Operation(summary = "查询所有系统")
    @GetMapping("/all")
    public Result<List<SystemVO>> all() {
        return Result.success(systemService.listAll());
    }

    @Operation(summary = "导入系统Excel")
    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(systemService.importExcel(file.getInputStream()));
    }

    @Operation(summary = "导出系统Excel")
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String sysNo,
            @RequestParam(required = false) String sysNameCn,
            @RequestParam(required = false) String sysType,
            @RequestParam(required = false) String sysOwner,
            @RequestParam(required = false) String sysLevel,
            HttpServletResponse response) throws IOException {
        systemService.exportExcel(sysNo, sysNameCn, sysType, sysOwner, sysLevel, response);
    }

    @Operation(summary = "下载系统导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        systemService.downloadTemplate(response);
    }
}
