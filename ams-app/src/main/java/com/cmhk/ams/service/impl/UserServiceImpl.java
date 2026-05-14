package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.User;
import com.cmhk.ams.model.dto.UserDTO;
import com.cmhk.ams.model.vo.UserVO;
import com.cmhk.ams.mapper.UserMapper;
import com.cmhk.ams.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserVO> page(int page, int pageSize, String username) {
        Page<User> p = new Page<>(page, pageSize);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> qw =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        qw.isNull(User::getDeletedAt);
        if (username != null && !username.isEmpty()) {
            qw.like(User::getUsername, username);
        }
        qw.orderByDesc(User::getCreatedAt);
        Page<User> userPage = userMapper.selectPage(p, qw);
        // 转换为VO（简化处理）
        Page<UserVO> voPage = new Page<>();
        voPage.setCurrent(userPage.getCurrent());
        voPage.setSize(userPage.getSize());
        voPage.setTotal(userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setCreatedAt(u.getCreatedAt());
            return vo;
        }).toList());
        return voPage;
    }

    @Override
    public UserVO detail(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setCreatedAt(user.getCreatedAt());
        return vo;
    }

    @Override
    public void create(UserDTO dto) {
        long count = userMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoleId(dto.getRoleId());
        userMapper.insert(user);
    }

    @Override
    public void update(Long id, UserDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRoleId(dto.getRoleId());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
