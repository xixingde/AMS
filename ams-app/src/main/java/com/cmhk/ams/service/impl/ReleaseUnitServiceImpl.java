package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.ReleaseUnit;
import com.cmhk.ams.model.dto.ReleaseUnitDTO;
import com.cmhk.ams.model.excel.ReleaseUnitExcel;
import com.cmhk.ams.model.vo.ReleaseUnitVO;
import com.cmhk.ams.mapper.ReleaseUnitMapper;
import com.cmhk.ams.mapper.ReleaseUnitTechStackMapper;
import com.cmhk.ams.service.ReleaseUnitService;
import com.cmhk.ams.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReleaseUnitServiceImpl implements ReleaseUnitService {

    private final ReleaseUnitMapper releaseUnitMapper;
    private final ReleaseUnitTechStackMapper releaseUnitTechStackMapper;

    @Override
    public Page<ReleaseUnitVO> page(int page, int pageSize, String deploySysNo, String unitNo, String unitNameCn, String unitType, String deployMode) {
        Page<ReleaseUnit> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<ReleaseUnit> qw = new LambdaQueryWrapper<>();
        qw.isNull(ReleaseUnit::getDeletedAt);
        if (deploySysNo != null && !deploySysNo.isEmpty()) qw.eq(ReleaseUnit::getDeploySysNo, deploySysNo);
        if (unitNo != null && !unitNo.isEmpty()) qw.like(ReleaseUnit::getUnitNo, unitNo);
        if (unitNameCn != null && !unitNameCn.isEmpty()) qw.like(ReleaseUnit::getUnitNameCn, unitNameCn);
        if (unitType != null && !unitType.isEmpty()) qw.eq(ReleaseUnit::getUnitType, unitType);
        if (deployMode != null && !deployMode.isEmpty()) qw.eq(ReleaseUnit::getDeployMode, deployMode);
        qw.orderByDesc(ReleaseUnit::getCreatedAt);
        Page<ReleaseUnit> res = releaseUnitMapper.selectPage(p, qw);
        Page<ReleaseUnitVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public ReleaseUnitVO detail(Long id) {
        ReleaseUnit u = releaseUnitMapper.selectById(id);
        if (u == null) throw new BusinessException("发布单元不存在");
        return toVO(u);
    }

    @Override
    @Transactional
    public void create(ReleaseUnitDTO dto) {
        ReleaseUnit u = new ReleaseUnit();
        BeanUtils.copyProperties(dto, u);
        releaseUnitMapper.insert(u);
        saveTechStacks(dto.getUnitNo(), dto.getStackNos());
    }

    @Override
    @Transactional
    public void update(Long id, ReleaseUnitDTO dto) {
        ReleaseUnit u = releaseUnitMapper.selectById(id);
        if (u == null) throw new BusinessException("发布单元不存在");
        String oldNo = u.getUnitNo();
        BeanUtils.copyProperties(dto, u, "id");
        releaseUnitMapper.updateById(u);
        releaseUnitTechStackMapper.deleteByUnitNo(oldNo);
        saveTechStacks(u.getUnitNo(), dto.getStackNos());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReleaseUnit u = releaseUnitMapper.selectById(id);
        if (u != null) {
            releaseUnitTechStackMapper.deleteByUnitNo(u.getUnitNo());
            releaseUnitMapper.deleteById(id);
        }
    }

    private void saveTechStacks(String unitNo, List<String> stackNos) {
        if (stackNos == null || stackNos.isEmpty()) return;
        for (String stackNo : stackNos) {
            com.cmhk.ams.model.ReleaseUnitTechStack ruts = new com.cmhk.ams.model.ReleaseUnitTechStack();
            ruts.setUnitNo(unitNo);
            ruts.setStackNo(stackNo);
            releaseUnitTechStackMapper.insert(ruts);
        }
    }

    @Override
    public Map<String, Object> importExcel(InputStream inputStream) {
        com.cmhk.ams.util.ExcelImportListener<ReleaseUnitExcel> listener = ExcelUtil.read(inputStream, ReleaseUnitExcel.class, list -> {
            for (ReleaseUnitExcel excel : list) {
                ReleaseUnit u = new ReleaseUnit();
                BeanUtils.copyProperties(excel, u);
                releaseUnitMapper.insert(u);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", listener.getSuccessCount());
        result.put("failCount", listener.getFailCount());
        result.put("failMessages", listener.getFailMessages());
        return result;
    }

    @Override
    public void exportExcel(String deploySysNo, String unitNo, String unitNameCn, String unitType, String deployMode, HttpServletResponse response) throws IOException {
        Page<ReleaseUnit> p = new Page<>(1, 10000);
        LambdaQueryWrapper<ReleaseUnit> qw = new LambdaQueryWrapper<>();
        qw.isNull(ReleaseUnit::getDeletedAt);
        if (deploySysNo != null && !deploySysNo.isEmpty()) qw.eq(ReleaseUnit::getDeploySysNo, deploySysNo);
        if (unitNo != null && !unitNo.isEmpty()) qw.like(ReleaseUnit::getUnitNo, unitNo);
        if (unitNameCn != null && !unitNameCn.isEmpty()) qw.like(ReleaseUnit::getUnitNameCn, unitNameCn);
        if (unitType != null && !unitType.isEmpty()) qw.eq(ReleaseUnit::getUnitType, unitType);
        if (deployMode != null && !deployMode.isEmpty()) qw.eq(ReleaseUnit::getDeployMode, deployMode);
        List<ReleaseUnit> list = releaseUnitMapper.selectPage(p, qw).getRecords();
        List<ReleaseUnitExcel> excelList = list.stream().map(item -> {
            ReleaseUnitExcel e = new ReleaseUnitExcel();
            BeanUtils.copyProperties(item, e);
            return e;
        }).toList();
        ExcelUtil.export(response, "发布单元清单", excelList, ReleaseUnitExcel.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.template(response, "发布单元清单", ReleaseUnitExcel.class);
    }

    private ReleaseUnitVO toVO(ReleaseUnit u) {
        ReleaseUnitVO vo = new ReleaseUnitVO();
        BeanUtils.copyProperties(u, vo);
        vo.setStackNos(releaseUnitMapper.selectStackNosByUnitNo(u.getUnitNo()));
        return vo;
    }
}
