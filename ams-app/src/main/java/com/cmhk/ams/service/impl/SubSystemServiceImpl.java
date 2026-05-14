package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.DeploySubSystem;
import com.cmhk.ams.model.SubSystem;
import com.cmhk.ams.model.dto.SubSystemDTO;
import com.cmhk.ams.model.excel.SubSystemExcel;
import com.cmhk.ams.model.vo.SubSystemVO;
import com.cmhk.ams.mapper.DeploySubSystemMapper;
import com.cmhk.ams.mapper.SubSystemMapper;
import com.cmhk.ams.service.SubSystemService;
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
public class SubSystemServiceImpl implements SubSystemService {

    private final SubSystemMapper subSystemMapper;
    private final DeploySubSystemMapper deploySubSystemMapper;

    @Override
    public Page<SubSystemVO> page(int page, int pageSize, String sysNo, String subSysNo, String subSysNameCn, String subSysLevel) {
        Page<SubSystem> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<SubSystem> qw = new LambdaQueryWrapper<>();
        qw.isNull(SubSystem::getDeletedAt);
        if (sysNo != null && !sysNo.isEmpty()) qw.eq(SubSystem::getSysNo, sysNo);
        if (subSysNo != null && !subSysNo.isEmpty()) qw.like(SubSystem::getSubSysNo, subSysNo);
        if (subSysNameCn != null && !subSysNameCn.isEmpty()) qw.like(SubSystem::getSubSysNameCn, subSysNameCn);
        if (subSysLevel != null && !subSysLevel.isEmpty()) qw.eq(SubSystem::getSubSysLevel, subSysLevel);
        qw.orderByDesc(SubSystem::getCreatedAt);
        Page<SubSystem> res = subSystemMapper.selectPage(p, qw);
        Page<SubSystemVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public SubSystemVO detail(Long id) {
        SubSystem s = subSystemMapper.selectById(id);
        if (s == null) throw new BusinessException("子系统不存在");
        return toVO(s);
    }

    @Override
    public void create(SubSystemDTO dto) {
        SubSystem s = new SubSystem();
        BeanUtils.copyProperties(dto, s);
        subSystemMapper.insert(s);
    }

    @Override
    public void update(Long id, SubSystemDTO dto) {
        SubSystem s = subSystemMapper.selectById(id);
        if (s == null) throw new BusinessException("子系统不存在");
        BeanUtils.copyProperties(dto, s, "id");
        subSystemMapper.updateById(s);
    }

    @Override
    public void delete(Long id) {
        SubSystem s = subSystemMapper.selectById(id);
        if (s == null) return;
        long count = deploySubSystemMapper.selectCount(new LambdaQueryWrapper<DeploySubSystem>().eq(DeploySubSystem::getSubSysNo, s.getSubSysNo()).isNull(DeploySubSystem::getDeletedAt));
        if (count > 0) throw new BusinessException("存在下级部署子系统，禁止删除");
        subSystemMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> importExcel(InputStream inputStream) {
        com.cmhk.ams.util.ExcelImportListener<SubSystemExcel> listener = ExcelUtil.read(inputStream, SubSystemExcel.class, list -> {
            for (SubSystemExcel excel : list) {
                SubSystem s = new SubSystem();
                BeanUtils.copyProperties(excel, s);
                subSystemMapper.insert(s);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", listener.getSuccessCount());
        result.put("failCount", listener.getFailCount());
        result.put("failMessages", listener.getFailMessages());
        return result;
    }

    @Override
    public void exportExcel(String sysNo, String subSysNo, String subSysNameCn, String subSysLevel, HttpServletResponse response) throws IOException {
        Page<SubSystem> p = new Page<>(1, 10000);
        LambdaQueryWrapper<SubSystem> qw = new LambdaQueryWrapper<>();
        qw.isNull(SubSystem::getDeletedAt);
        if (sysNo != null && !sysNo.isEmpty()) qw.eq(SubSystem::getSysNo, sysNo);
        if (subSysNo != null && !subSysNo.isEmpty()) qw.like(SubSystem::getSubSysNo, subSysNo);
        if (subSysNameCn != null && !subSysNameCn.isEmpty()) qw.like(SubSystem::getSubSysNameCn, subSysNameCn);
        if (subSysLevel != null && !subSysLevel.isEmpty()) qw.eq(SubSystem::getSubSysLevel, subSysLevel);
        List<SubSystem> list = subSystemMapper.selectPage(p, qw).getRecords();
        List<SubSystemExcel> excelList = list.stream().map(item -> {
            SubSystemExcel e = new SubSystemExcel();
            BeanUtils.copyProperties(item, e);
            return e;
        }).toList();
        ExcelUtil.export(response, "子系统清单", excelList, SubSystemExcel.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.template(response, "子系统清单", SubSystemExcel.class);
    }

    private SubSystemVO toVO(SubSystem s) {
        SubSystemVO vo = new SubSystemVO();
        BeanUtils.copyProperties(s, vo);
        return vo;
    }
}
