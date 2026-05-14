package com.cmhk.ams.controller;

import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.vo.RoleVO;
import com.cmhk.ams.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "查询角色列表")
    @GetMapping
    public Result<List<RoleVO>> list() {
        return Result.success(roleService.list());
    }
}
