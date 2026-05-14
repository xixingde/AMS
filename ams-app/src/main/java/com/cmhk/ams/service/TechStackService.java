package com.cmhk.ams.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.model.dto.TechStackDTO;
import com.cmhk.ams.model.vo.TechStackVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface TechStackService {
    Page<TechStackVO> page(int page, int pageSize, String stackNo, String category, String name, String selectionAdvice, String responsibleLine);
    TechStackVO detail(Long id);
    void create(TechStackDTO dto);
    void update(Long id, TechStackDTO dto);
    void delete(Long id);
    Map<String, Object> importExcel(InputStream inputStream);
    void exportExcel(String stackNo, String category, String name, String selectionAdvice, String responsibleLine, HttpServletResponse response) throws IOException;
    void downloadTemplate(HttpServletResponse response) throws IOException;
}
