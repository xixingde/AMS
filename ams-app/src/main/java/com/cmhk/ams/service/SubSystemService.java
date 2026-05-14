package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.SubSystemDTO;
import com.cmhk.ams.model.vo.SubSystemVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface SubSystemService {
    Page<SubSystemVO> page(int page, int pageSize, String sysNo, String subSysNo, String subSysNameCn, String subSysLevel);
    SubSystemVO detail(Long id);
    void create(SubSystemDTO dto);
    void update(Long id, SubSystemDTO dto);
    void delete(Long id);
    Map<String, Object> importExcel(InputStream inputStream);
    void exportExcel(String sysNo, String subSysNo, String subSysNameCn, String subSysLevel, HttpServletResponse response) throws IOException;
    void downloadTemplate(HttpServletResponse response) throws IOException;
}
