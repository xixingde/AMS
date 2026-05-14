package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.SystemDTO;
import com.cmhk.ams.model.vo.SystemVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SystemService {
    Page<SystemVO> page(int page, int pageSize, String sysNo, String sysNameCn, String sysType, String sysOwner, String sysLevel);
    SystemVO detail(Long id);
    void create(SystemDTO dto);
    void update(Long id, SystemDTO dto);
    void delete(Long id);
    List<SystemVO> listAll();
    Map<String, Object> importExcel(InputStream inputStream);
    void exportExcel(String sysNo, String sysNameCn, String sysType, String sysOwner, String sysLevel, HttpServletResponse response) throws IOException;
    void downloadTemplate(HttpServletResponse response) throws IOException;
}
