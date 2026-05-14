package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.ReleaseUnitRelationDTO;
import com.cmhk.ams.model.vo.ReleaseUnitRelationVO;

public interface ReleaseUnitRelationService {
    Page<ReleaseUnitRelationVO> page(int page, int pageSize, String deploySysNo, String callerUnitNo, String calleeUnitNo, String protocol);
    ReleaseUnitRelationVO detail(Long id);
    void create(ReleaseUnitRelationDTO dto);
    void update(Long id, ReleaseUnitRelationDTO dto);
    void delete(Long id);
}
