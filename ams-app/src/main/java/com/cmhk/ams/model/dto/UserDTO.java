package com.cmhk.ams.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Long roleId;
}
