package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.DeploySubSystem;
import com.cmhk.ams.model.ReleaseUnit;
import com.cmhk.ams.model.dto.DeploySubSystemDTO;
import com.cmhk.ams.model.excel.DeploySubSystemExcel;
import com.cmhk.ams.model.vo.DeploySubSystemVO;
import com.cmhk.ams.mapper.DeploySubSystemMapper;
import com.cmhk.ams.mapper.ReleaseUnitMapper;
import com.cmhk.ams.service.DeploySubSystemService;
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
public class DeploySubSystemServiceImpl implements DeploySubSystemService {

    private final DeploySubSystemMapper deploySubSystemMapper;
    private final ReleaseUnitMapper releaseUnitMapper;

    @Override
    public Page<DeploySubSystemVO> page(int page, int pageSize, String subSysNo, String deploySysNo, String deploySysNameCn) {
        Page<DeploySubSystem> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<DeploySubSystem> qw = new LambdaQueryWrapper<>();
        qw.isNull(DeploySubSystem::getDeletedAt);
        if (subSysNo != null && !subSysNo.isEmpty()) qw.eq(DeploySubSystem::getSubSysNo, subSysNo);
        if (deploySysNo != null && !deploySysNo.isEmpty()) qw.like(DeploySubSystem::getDeploySysNo, deploySysNo);
        if (deploySysNameCn != null && !deploySysNameCn.isEmpty()) qw.like(DeploySubSystem::getDeploySysNameCn, deploySysNameCn);
        qw.orderByDesc(DeploySubSystem::getCreatedAt);
        Page<DeploySubSystem> res = deploySubSystemMapper.selectPage(p, qw);
        Page<DeploySubSystemVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public DeploySubSystemVO detail(Long id) {
        DeploySubSystem d = deploySubSystemMapper.selectById(id);
        if (d == null) throw new BusinessException("部署子系统不存在");
        return toVO(d);
    }

    @Override
    public void create(DeploySubSystemDTO dto) {
        DeploySubSystem d = new DeploySubSystem();
        BeanUtils.copyProperties(dto, d);
        deploySubSystemMapper.insert(d);
    }

    @Override
    public void update(Long id, DeploySubSystemDTO dto) {
        DeploySubSystem d = deploySubSystemMapper.selectById(id);
        if (d == null) throw new BusinessException("部署子系统不存在");
        BeanUtils.copyProperties(dto, d, "id");
        deploySubSystemMapper.updateById(d);
    }

    @Override
    public void delete(Long id) {
        DeploySubSystem d = deploySubSystemMapper.selectById(id);
        if (d == null) return;
        long count = releaseUnitMapper.selectCount(new LambdaQueryWrapper<ReleaseUnit>().eq(ReleaseUnit::getDeploySysNo, d.getDeploySysNo()).isNull(ReleaseUnit::getDeletedAt));
        if (count > 0) throw new BusinessException("存在下级发布单元，禁止删除");
        deploySubSystemMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> importExcel(InputStream inputStream) {
        com.cmhk.ams.util.ExcelImportListener<DeploySubSystemExcel> listener = ExcelUtil.read(inputStream, DeploySubSystemExcel.class, list -> {
            for (DeploySubSystemExcel excel : list) {
                DeploySubSystem d = new DeploySubSystem();
                BeanUtils.copyProperties(excel, d);
                deploySubSystemMapper.insert(d);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", listener.getSuccessCount());
        result.put("failCount", listener.getFailCount());
        result.put("failMessages", listener.getFailMessages());
        return result;
    }

    @Override
    public void exportExcel(String subSysNo, String deploySysNo, String deploySysNameCn, HttpServletResponse response) throws IOException {
        Page<DeploySubSystem> p = new Page<>(1, 10000);
        LambdaQueryWrapper<DeploySubSystem> qw = new LambdaQueryWrapper<>();
        qw.isNull(DeploySubSystem::getDeletedAt);
        if (subSysNo != null && !subSysNo.isEmpty()) qw.eq(DeploySubSystem::getSubSysNo, subSysNo);
        if (deploySysNo != null && !deploySysNo.isEmpty()) qw.like(DeploySubSystem::getDeploySysNo, deploySysNo);
        if (deploySysNameCn != null && !deploySysNameCn.isEmpty()) qw.like(DeploySubSystem::getDeploySysNameCn, deploySysNameCn);
        List<DeploySubSystem> list = deploySubSystemMapper.selectPage(p, qw).getRecords();
        List<DeploySubSystemExcel> excelList = list.stream().map(item -> {
            DeploySubSystemExcel e = new DeploySubSystemExcel();
            BeanUtils.copyProperties(item, e);
            return e;
        }).toList();
        ExcelUtil.export(response, "部署子系统清单", excelList, DeploySubSystemExcel.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.template(response, "部署子系统清单", DeploySubSystemExcel.class);
    }

    private DeploySubSystemVO toVO(DeploySubSystem d) {
        DeploySubSystemVO vo = new DeploySubSystemVO();
        BeanUtils.copyProperties(d, vo);
        return vo;
    }
}
