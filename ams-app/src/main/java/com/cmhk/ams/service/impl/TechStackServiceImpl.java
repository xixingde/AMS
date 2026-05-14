package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.TechStack;
import com.cmhk.ams.model.dto.TechStackDTO;
import com.cmhk.ams.model.excel.TechStackExcel;
import com.cmhk.ams.model.vo.TechStackVO;
import com.cmhk.ams.mapper.TechStackMapper;
import com.cmhk.ams.service.TechStackService;
import com.cmhk.ams.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TechStackServiceImpl implements TechStackService {

    private final TechStackMapper techStackMapper;

    @Override
    public Page<TechStackVO> page(int page, int pageSize, String stackNo, String category, String name, String selectionAdvice, String responsibleLine) {
        Page<TechStack> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<TechStack> qw = new LambdaQueryWrapper<>();
        qw.isNull(TechStack::getDeletedAt);
        if (stackNo != null && !stackNo.isEmpty()) qw.like(TechStack::getStackNo, stackNo);
        if (category != null && !category.isEmpty()) qw.eq(TechStack::getCategory, category);
        if (name != null && !name.isEmpty()) qw.like(TechStack::getName, name);
        if (selectionAdvice != null && !selectionAdvice.isEmpty()) qw.eq(TechStack::getSelectionAdvice, selectionAdvice);
        if (responsibleLine != null && !responsibleLine.isEmpty()) qw.eq(TechStack::getResponsibleLine, responsibleLine);
        qw.orderByDesc(TechStack::getCreatedAt);
        Page<TechStack> res = techStackMapper.selectPage(p, qw);
        Page<TechStackVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public TechStackVO detail(Long id) {
        TechStack ts = techStackMapper.selectById(id);
        if (ts == null) throw new BusinessException("技术栈不存在");
        return toVO(ts);
    }

    @Override
    public void create(TechStackDTO dto) {
        TechStack ts = new TechStack();
        BeanUtils.copyProperties(dto, ts);
        techStackMapper.insert(ts);
    }

    @Override
    public void update(Long id, TechStackDTO dto) {
        TechStack ts = techStackMapper.selectById(id);
        if (ts == null) throw new BusinessException("技术栈不存在");
        BeanUtils.copyProperties(dto, ts, "id");
        techStackMapper.updateById(ts);
    }

    @Override
    public void delete(Long id) {
        TechStack ts = techStackMapper.selectById(id);
        if (ts == null) return;
        long count = techStackMapper.countRefByStackNo(ts.getStackNo());
        if (count > 0) throw new BusinessException("该技术栈已被发布单元引用，无法删除");
        techStackMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> importExcel(InputStream inputStream) {
        com.cmhk.ams.util.ExcelImportListener<TechStackExcel> listener = ExcelUtil.read(inputStream, TechStackExcel.class, list -> {
            for (TechStackExcel excel : list) {
                TechStack ts = new TechStack();
                BeanUtils.copyProperties(excel, ts);
                techStackMapper.insert(ts);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", listener.getSuccessCount());
        result.put("failCount", listener.getFailCount());
        result.put("failMessages", listener.getFailMessages());
        return result;
    }

    @Override
    public void exportExcel(String stackNo, String category, String name, String selectionAdvice, String responsibleLine, HttpServletResponse response) throws IOException {
        Page<TechStack> p = new Page<>(1, 10000);
        LambdaQueryWrapper<TechStack> qw = new LambdaQueryWrapper<>();
        qw.isNull(TechStack::getDeletedAt);
        if (stackNo != null && !stackNo.isEmpty()) qw.like(TechStack::getStackNo, stackNo);
        if (category != null && !category.isEmpty()) qw.eq(TechStack::getCategory, category);
        if (name != null && !name.isEmpty()) qw.like(TechStack::getName, name);
        if (selectionAdvice != null && !selectionAdvice.isEmpty()) qw.eq(TechStack::getSelectionAdvice, selectionAdvice);
        if (responsibleLine != null && !responsibleLine.isEmpty()) qw.eq(TechStack::getResponsibleLine, responsibleLine);
        List<TechStack> list = techStackMapper.selectPage(p, qw).getRecords();
        List<TechStackExcel> excelList = list.stream().map(item -> {
            TechStackExcel e = new TechStackExcel();
            BeanUtils.copyProperties(item, e);
            return e;
        }).toList();
        ExcelUtil.export(response, "技术栈清单", excelList, TechStackExcel.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.template(response, "技术栈清单", TechStackExcel.class);
    }

    private TechStackVO toVO(TechStack ts) {
        TechStackVO vo = new TechStackVO();
        BeanUtils.copyProperties(ts, vo);
        return vo;
    }
}
