package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.SubSystemDTO;
import com.cmhk.ams.model.vo.SubSystemVO;
import com.cmhk.ams.service.SubSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "子系统管理")
@RestController
@RequestMapping("/api/v1/sub-systems")
@RequiredArgsConstructor
public class SubSystemController {

    private final SubSystemService subSystemService;

    @Operation(summary = "查询子系统列表")
    @GetMapping
    public Result<Page<SubSystemVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sysNo,
            @RequestParam(required = false) String subSysNo,
            @RequestParam(required = false) String subSysNameCn,
            @RequestParam(required = false) String subSysLevel) {
        return Result.success(subSystemService.page(page, pageSize, sysNo, subSysNo, subSysNameCn, subSysLevel));
    }

    @Operation(summary = "查询子系统详情")
    @GetMapping("/{id}")
    public Result<SubSystemVO> detail(@PathVariable Long id) {
        return Result.success(subSystemService.detail(id));
    }

    @Operation(summary = "创建子系统")
    @PostMapping
    public Result<Void> create(@RequestBody SubSystemDTO dto) {
        subSystemService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新子系统")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SubSystemDTO dto) {
        subSystemService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除子系统")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        subSystemService.delete(id);
        return Result.success();
    }

    @Operation(summary = "导入子系统Excel")
    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(subSystemService.importExcel(file.getInputStream()));
    }

    @Operation(summary = "导出子系统Excel")
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String sysNo,
            @RequestParam(required = false) String subSysNo,
            @RequestParam(required = false) String subSysNameCn,
            @RequestParam(required = false) String subSysLevel,
            HttpServletResponse response) throws IOException {
        subSystemService.exportExcel(sysNo, subSysNo, subSysNameCn, subSysLevel, response);
    }

    @Operation(summary = "下载子系统导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        subSystemService.downloadTemplate(response);
    }
}
