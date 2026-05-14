package com.cmhk.ams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmhk.ams.model.DiagramLayout;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DiagramLayoutMapper extends BaseMapper<DiagramLayout> {

    @Select("SELECT * FROM diagram_layouts WHERE deploy_sys_no = #{deploySysNo} LIMIT 1")
    DiagramLayout selectByDeploySysNo(@Param("deploySysNo") String deploySysNo);
}
