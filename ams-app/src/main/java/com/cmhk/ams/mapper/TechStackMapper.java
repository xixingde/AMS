package com.cmhk.ams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmhk.ams.model.TechStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TechStackMapper extends BaseMapper<TechStack> {

    @Select("SELECT COUNT(*) FROM release_unit_tech_stack WHERE stack_no = #{stackNo}")
    long countRefByStackNo(@Param("stackNo") String stackNo);
}
