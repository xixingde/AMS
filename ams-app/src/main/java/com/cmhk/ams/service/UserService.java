package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.UserDTO;
import com.cmhk.ams.model.vo.UserVO;

import java.util.List;

public interface UserService {
    Page<UserVO> page(int page, int pageSize, String username);
    UserVO detail(Long id);
    void create(UserDTO dto);
    void update(Long id, UserDTO dto);
    void delete(Long id);
}
