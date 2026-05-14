package com.cmhk.ams.service.impl;

import com.cmhk.ams.model.Role;
import com.cmhk.ams.model.vo.RoleVO;
import com.cmhk.ams.mapper.RoleMapper;
import com.cmhk.ams.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<RoleVO> list() {
        List<Role> roles = roleMapper.selectList(null);
        return roles.stream().map(r -> {
            RoleVO vo = new RoleVO();
            vo.setId(r.getId());
            vo.setRoleName(r.getRoleName());
            vo.setRoleCode(r.getRoleCode());
            return vo;
        }).toList();
    }
}
