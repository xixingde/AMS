package com.cmhk.ams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmhk.ams.model.ReleaseUnitTechStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface ReleaseUnitTechStackMapper extends BaseMapper<ReleaseUnitTechStack> {

    @Delete("DELETE FROM release_unit_tech_stack WHERE unit_no = #{unitNo}")
    void deleteByUnitNo(@Param("unitNo") String unitNo);
}
