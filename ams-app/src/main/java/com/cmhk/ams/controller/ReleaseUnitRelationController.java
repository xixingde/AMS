package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.ReleaseUnitRelationDTO;
import com.cmhk.ams.model.vo.ReleaseUnitRelationVO;
import com.cmhk.ams.service.ReleaseUnitRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "发布单元调用关系管理")
@RestController
@RequestMapping("/api/v1/release-unit-relations")
@RequiredArgsConstructor
public class ReleaseUnitRelationController {

    private final ReleaseUnitRelationService relationService;

    @Operation(summary = "查询调用关系列表")
    @GetMapping
    public Result<Page<ReleaseUnitRelationVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String deploySysNo,
            @RequestParam(required = false) String callerUnitNo,
            @RequestParam(required = false) String calleeUnitNo,
            @RequestParam(required = false) String protocol) {
        return Result.success(relationService.page(page, pageSize, deploySysNo, callerUnitNo, calleeUnitNo, protocol));
    }

    @Operation(summary = "查询调用关系详情")
    @GetMapping("/{id}")
    public Result<ReleaseUnitRelationVO> detail(@PathVariable Long id) {
        return Result.success(relationService.detail(id));
    }

    @Operation(summary = "创建调用关系")
    @PostMapping
    public Result<Void> create(@RequestBody ReleaseUnitRelationDTO dto) {
        relationService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新调用关系")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ReleaseUnitRelationDTO dto) {
        relationService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除调用关系")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        relationService.delete(id);
        return Result.success();
    }
}
