package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.DeploySubSystemDTO;
import com.cmhk.ams.model.vo.DeploySubSystemVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface DeploySubSystemService {
    Page<DeploySubSystemVO> page(int page, int pageSize, String subSysNo, String deploySysNo, String deploySysNameCn);
    DeploySubSystemVO detail(Long id);
    void create(DeploySubSystemDTO dto);
    void update(Long id, DeploySubSystemDTO dto);
    void delete(Long id);
    Map<String, Object> importExcel(InputStream inputStream);
    void exportExcel(String subSysNo, String deploySysNo, String deploySysNameCn, HttpServletResponse response) throws IOException;
    void downloadTemplate(HttpServletResponse response) throws IOException;
}
