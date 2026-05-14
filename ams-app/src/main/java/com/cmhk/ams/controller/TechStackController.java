package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.TechStackDTO;
import com.cmhk.ams.model.vo.TechStackVO;
import com.cmhk.ams.service.TechStackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "技术栈管理")
@RestController
@RequestMapping("/api/v1/tech-stacks")
@RequiredArgsConstructor
public class TechStackController {

    private final TechStackService techStackService;

    @Operation(summary = "查询技术栈列表")
    @GetMapping
    public Result<Page<TechStackVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String stackNo,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String selectionAdvice,
            @RequestParam(required = false) String responsibleLine) {
        return Result.success(techStackService.page(page, pageSize, stackNo, category, name, selectionAdvice, responsibleLine));
    }

    @Operation(summary = "查询技术栈详情")
    @GetMapping("/{id}")
    public Result<TechStackVO> detail(@PathVariable Long id) {
        return Result.success(techStackService.detail(id));
    }

    @Operation(summary = "创建技术栈")
    @PostMapping
    public Result<Void> create(@RequestBody TechStackDTO dto) {
        techStackService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新技术栈")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TechStackDTO dto) {
        techStackService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除技术栈")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        techStackService.delete(id);
        return Result.success();
    }

    @Operation(summary = "导入技术栈Excel")
    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(techStackService.importExcel(file.getInputStream()));
    }

    @Operation(summary = "导出技术栈Excel")
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String stackNo,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String selectionAdvice,
            @RequestParam(required = false) String responsibleLine,
            HttpServletResponse response) throws IOException {
        techStackService.exportExcel(stackNo, category, name, selectionAdvice, responsibleLine, response);
    }

    @Operation(summary = "下载技术栈导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        techStackService.downloadTemplate(response);
    }
}
