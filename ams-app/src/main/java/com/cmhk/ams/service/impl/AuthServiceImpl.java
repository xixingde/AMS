package com.cmhk.ams.service.impl;

import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.Role;
import com.cmhk.ams.model.User;
import com.cmhk.ams.model.dto.UserDTO;
import com.cmhk.ams.mapper.RoleMapper;
import com.cmhk.ams.mapper.UserMapper;
import com.cmhk.ams.security.JwtUtil;
import com.cmhk.ams.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Result<Map<String, Object>> login(UserDTO dto) {
        User user = userMapper.selectById(1L);
        // 查询用户
        // 由于MyBatis Plus的BaseMapper没有自定义方法，先通过lambda查询
        // 这里简化处理
        user = userMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())
                .isNull(User::getDeletedAt)
        ).stream().findFirst().orElse(null);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        Role role = roleMapper.selectById(user.getRoleId());
        String roleCode = role != null ? role.getRoleCode() : "USER";
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), roleCode);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "role", roleCode
        ));
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> me(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        Role role = roleMapper.selectById(user.getRoleId());
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", role != null ? role.getRoleCode() : "USER");
        data.put("roleName", role != null ? role.getRoleName() : "");
        return Result.success(data);
    }
}
