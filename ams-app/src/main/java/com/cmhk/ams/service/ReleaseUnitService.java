package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.ReleaseUnitDTO;
import com.cmhk.ams.model.vo.ReleaseUnitVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ReleaseUnitService {
    Page<ReleaseUnitVO> page(int page, int pageSize, String deploySysNo, String unitNo, String unitNameCn, String unitType, String deployMode);
    ReleaseUnitVO detail(Long id);
    void create(ReleaseUnitDTO dto);
    void update(Long id, ReleaseUnitDTO dto);
    void delete(Long id);
    Map<String, Object> importExcel(InputStream inputStream);
    void exportExcel(String deploySysNo, String unitNo, String unitNameCn, String unitType, String deployMode, HttpServletResponse response) throws IOException;
    void downloadTemplate(HttpServletResponse response) throws IOException;
}
