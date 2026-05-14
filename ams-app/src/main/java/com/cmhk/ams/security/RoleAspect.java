package com.cmhk.ams.security;

import com.cmhk.ams.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAspect {

    private final JwtUtil jwtUtil;

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        String token = resolveToken();
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new BusinessException(403, "未登录或Token无效");
        }
        String role = jwtUtil.getRole(token);
        String[] required = requireRole.value();
        if (required.length > 0 && Arrays.stream(required).noneMatch(r -> r.equals(role))) {
            throw new BusinessException(403, "无权限执行此操作");
        }
    }

    private String resolveToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest request = attrs.getRequest();
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
