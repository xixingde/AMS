package com.cmhk.ams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmhk.ams.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT u.*, r.role_name as roleName, r.role_code as roleCode FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.username = #{username} AND u.deleted_at IS NULL LIMIT 1")
    User findByUsername(@Param("username") String username);
}
