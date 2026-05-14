package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.ReleaseUnitDTO;
import com.cmhk.ams.model.vo.ReleaseUnitVO;
import com.cmhk.ams.service.ReleaseUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "发布单元管理")
@RestController
@RequestMapping("/api/v1/release-units")
@RequiredArgsConstructor
public class ReleaseUnitController {

    private final ReleaseUnitService releaseUnitService;

    @Operation(summary = "查询发布单元列表")
    @GetMapping
    public Result<Page<ReleaseUnitVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String deploySysNo,
            @RequestParam(required = false) String unitNo,
            @RequestParam(required = false) String unitNameCn,
            @RequestParam(required = false) String unitType,
            @RequestParam(required = false) String deployMode) {
        return Result.success(releaseUnitService.page(page, pageSize, deploySysNo, unitNo, unitNameCn, unitType, deployMode));
    }

    @Operation(summary = "查询发布单元详情")
    @GetMapping("/{id}")
    public Result<ReleaseUnitVO> detail(@PathVariable Long id) {
        return Result.success(releaseUnitService.detail(id));
    }

    @Operation(summary = "创建发布单元")
    @PostMapping
    public Result<Void> create(@RequestBody ReleaseUnitDTO dto) {
        releaseUnitService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新发布单元")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ReleaseUnitDTO dto) {
        releaseUnitService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除发布单元")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        releaseUnitService.delete(id);
        return Result.success();
    }

    @Operation(summary = "导入发布单元Excel")
    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(releaseUnitService.importExcel(file.getInputStream()));
    }

    @Operation(summary = "导出发布单元Excel")
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String deploySysNo,
            @RequestParam(required = false) String unitNo,
            @RequestParam(required = false) String unitNameCn,
            @RequestParam(required = false) String unitType,
            @RequestParam(required = false) String deployMode,
            HttpServletResponse response) throws IOException {
        releaseUnitService.exportExcel(deploySysNo, unitNo, unitNameCn, unitType, deployMode, response);
    }

    @Operation(summary = "下载发布单元导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        releaseUnitService.downloadTemplate(response);
    }
}
