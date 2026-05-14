package com.cmhk.ams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmhk.ams.model.ReleaseUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReleaseUnitMapper extends BaseMapper<ReleaseUnit> {

    @Select("SELECT stack_no FROM release_unit_tech_stack WHERE unit_no = #{unitNo}")
    List<String> selectStackNosByUnitNo(@Param("unitNo") String unitNo);
}
