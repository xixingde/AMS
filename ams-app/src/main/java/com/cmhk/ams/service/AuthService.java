package com.cmhk.ams.service;

import com.cmhk.ams.model.Result;
import com.cmhk.ams.model.dto.UserDTO;

import java.util.Map;

public interface AuthService {
    Result<Map<String, Object>> login(UserDTO dto);
    Result<Map<String, Object>> me(Long userId);
}
