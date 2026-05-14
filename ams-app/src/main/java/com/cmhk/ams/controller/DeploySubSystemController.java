package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.DeploySubSystemDTO;
import com.cmhk.ams.model.vo.DeploySubSystemVO;
import com.cmhk.ams.service.DeploySubSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "部署子系统管理")
@RestController
@RequestMapping("/api/v1/deploy-sub-systems")
@RequiredArgsConstructor
public class DeploySubSystemController {

    private final DeploySubSystemService deploySubSystemService;

    @Operation(summary = "查询部署子系统列表")
    @GetMapping
    public Result<Page<DeploySubSystemVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String subSysNo,
            @RequestParam(required = false) String deploySysNo,
            @RequestParam(required = false) String deploySysNameCn) {
        return Result.success(deploySubSystemService.page(page, pageSize, subSysNo, deploySysNo, deploySysNameCn));
    }

    @Operation(summary = "查询部署子系统详情")
    @GetMapping("/{id}")
    public Result<DeploySubSystemVO> detail(@PathVariable Long id) {
        return Result.success(deploySubSystemService.detail(id));
    }

    @Operation(summary = "创建部署子系统")
    @PostMapping
    public Result<Void> create(@RequestBody DeploySubSystemDTO dto) {
        deploySubSystemService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新部署子系统")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody DeploySubSystemDTO dto) {
        deploySubSystemService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除部署子系统")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deploySubSystemService.delete(id);
        return Result.success();
    }

    @Operation(summary = "导入部署子系统Excel")
    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(deploySubSystemService.importExcel(file.getInputStream()));
    }

    @Operation(summary = "导出部署子系统Excel")
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String subSysNo,
            @RequestParam(required = false) String deploySysNo,
            @RequestParam(required = false) String deploySysNameCn,
            HttpServletResponse response) throws IOException {
        deploySubSystemService.exportExcel(subSysNo, deploySysNo, deploySysNameCn, response);
    }

    @Operation(summary = "下载部署子系统导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        deploySubSystemService.downloadTemplate(response);
    }
}
