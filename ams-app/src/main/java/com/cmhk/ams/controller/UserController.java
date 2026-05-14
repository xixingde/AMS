package com.cmhk.ams.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.UserDTO;
import com.cmhk.ams.model.vo.UserVO;
import com.cmhk.ams.security.RequireRole;
import com.cmhk.ams.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "查询用户列表")
    @GetMapping
    public Result<Page<UserVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String username) {
        return Result.success(userService.page(page, pageSize, username));
    }

    @Operation(summary = "查询用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable Long id) {
        return Result.success(userService.detail(id));
    }

    @Operation(summary = "创建用户")
    @PostMapping
    @RequireRole({"SUPER_ADMIN"})
    public Result<Void> create(@RequestBody UserDTO dto) {
        userService.create(dto);
        return Result.success();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @RequireRole({"SUPER_ADMIN"})
    public Result<Void> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        userService.update(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @RequireRole({"SUPER_ADMIN"})
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
