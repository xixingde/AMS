package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.SubSystem;
import com.cmhk.ams.model.System;
import com.cmhk.ams.model.dto.SystemDTO;
import com.cmhk.ams.model.excel.SystemExcel;
import com.cmhk.ams.model.vo.SystemVO;
import com.cmhk.ams.mapper.SubSystemMapper;
import com.cmhk.ams.mapper.SystemMapper;
import com.cmhk.ams.service.SystemService;
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
public class SystemServiceImpl implements SystemService {

    private final SystemMapper systemMapper;
    private final SubSystemMapper subSystemMapper;

    @Override
    public Page<SystemVO> page(int page, int pageSize, String sysNo, String sysNameCn, String sysType, String sysOwner, String sysLevel) {
        Page<System> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<System> qw = new LambdaQueryWrapper<>();
        qw.isNull(System::getDeletedAt);
        if (sysNo != null && !sysNo.isEmpty()) qw.like(System::getSysNo, sysNo);
        if (sysNameCn != null && !sysNameCn.isEmpty()) qw.like(System::getSysNameCn, sysNameCn);
        if (sysType != null && !sysType.isEmpty()) qw.eq(System::getSysType, sysType);
        if (sysOwner != null && !sysOwner.isEmpty()) qw.like(System::getSysOwner, sysOwner);
        if (sysLevel != null && !sysLevel.isEmpty()) qw.eq(System::getSysLevel, sysLevel);
        qw.orderByDesc(System::getCreatedAt);
        Page<System> res = systemMapper.selectPage(p, qw);
        Page<SystemVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public SystemVO detail(Long id) {
        System sys = systemMapper.selectById(id);
        if (sys == null) throw new BusinessException("系统不存在");
        return toVO(sys);
    }

    @Override
    public void create(SystemDTO dto) {
        System sys = new System();
        BeanUtils.copyProperties(dto, sys);
        systemMapper.insert(sys);
    }

    @Override
    public void update(Long id, SystemDTO dto) {
        System sys = systemMapper.selectById(id);
        if (sys == null) throw new BusinessException("系统不存在");
        BeanUtils.copyProperties(dto, sys, "id");
        systemMapper.updateById(sys);
    }

    @Override
    public void delete(Long id) {
        long count = subSystemMapper.selectCount(new LambdaQueryWrapper<SubSystem>().eq(SubSystem::getSysNo, systemMapper.selectById(id).getSysNo()).isNull(SubSystem::getDeletedAt));
        if (count > 0) throw new BusinessException("存在下级子系统，禁止删除");
        systemMapper.deleteById(id);
    }

    @Override
    public java.util.List<SystemVO> listAll() {
        return systemMapper.selectList(new LambdaQueryWrapper<System>().isNull(System::getDeletedAt))
            .stream().map(this::toVO).toList();
    }

    private SystemVO toVO(System sys) {
        SystemVO vo = new SystemVO();
        BeanUtils.copyProperties(sys, vo);
        return vo;
    }

    @Override
    public Map<String, Object> importExcel(InputStream inputStream) {
        com.cmhk.ams.util.ExcelImportListener<SystemExcel> listener = ExcelUtil.read(inputStream, SystemExcel.class, list -> {
            for (SystemExcel excel : list) {
                System sys = new System();
                BeanUtils.copyProperties(excel, sys);
                systemMapper.insert(sys);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", listener.getSuccessCount());
        result.put("failCount", listener.getFailCount());
        result.put("failMessages", listener.getFailMessages());
        return result;
    }

    @Override
    public void exportExcel(String sysNo, String sysNameCn, String sysType, String sysOwner, String sysLevel, HttpServletResponse response) throws IOException {
        Page<System> p = new Page<>(1, 10000);
        LambdaQueryWrapper<System> qw = new LambdaQueryWrapper<>();
        qw.isNull(System::getDeletedAt);
        if (sysNo != null && !sysNo.isEmpty()) qw.like(System::getSysNo, sysNo);
        if (sysNameCn != null && !sysNameCn.isEmpty()) qw.like(System::getSysNameCn, sysNameCn);
        if (sysType != null && !sysType.isEmpty()) qw.eq(System::getSysType, sysType);
        if (sysOwner != null && !sysOwner.isEmpty()) qw.like(System::getSysOwner, sysOwner);
        if (sysLevel != null && !sysLevel.isEmpty()) qw.eq(System::getSysLevel, sysLevel);
        List<System> list = systemMapper.selectPage(p, qw).getRecords();
        List<SystemExcel> excelList = list.stream().map(item -> {
            SystemExcel e = new SystemExcel();
            BeanUtils.copyProperties(item, e);
            return e;
        }).toList();
        ExcelUtil.export(response, "系统清单", excelList, SystemExcel.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.template(response, "系统清单", SystemExcel.class);
    }
}
