package com.cmhk.ams.controller;

import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.UserDTO;
import com.cmhk.ams.security.JwtUtil;
import com.cmhk.ams.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody UserDTO dto) {
        return authService.login(dto);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<Map<String, Object>> me(HttpServletRequest request) {
        String token = resolveToken(request);
        Long userId = jwtUtil.getUserId(token);
        return authService.me(userId);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
