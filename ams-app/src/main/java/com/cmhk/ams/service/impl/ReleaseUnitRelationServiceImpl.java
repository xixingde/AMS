package com.cmhk.ams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmhk.ams.exception.BusinessException;
import com.cmhk.ams.model.ReleaseUnitRelation;
import com.cmhk.ams.model.dto.ReleaseUnitRelationDTO;
import com.cmhk.ams.model.vo.ReleaseUnitRelationVO;
import com.cmhk.ams.mapper.ReleaseUnitRelationMapper;
import com.cmhk.ams.service.ReleaseUnitRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReleaseUnitRelationServiceImpl implements ReleaseUnitRelationService {

    private final ReleaseUnitRelationMapper relationMapper;

    @Override
    public Page<ReleaseUnitRelationVO> page(int page, int pageSize, String deploySysNo, String callerUnitNo, String calleeUnitNo, String protocol) {
        Page<ReleaseUnitRelation> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<ReleaseUnitRelation> qw = new LambdaQueryWrapper<>();
        qw.isNull(ReleaseUnitRelation::getDeletedAt);
        if (deploySysNo != null && !deploySysNo.isEmpty()) qw.eq(ReleaseUnitRelation::getDeploySysNo, deploySysNo);
        if (callerUnitNo != null && !callerUnitNo.isEmpty()) qw.like(ReleaseUnitRelation::getCallerUnitNo, callerUnitNo);
        if (calleeUnitNo != null && !calleeUnitNo.isEmpty()) qw.like(ReleaseUnitRelation::getCalleeUnitNo, calleeUnitNo);
        if (protocol != null && !protocol.isEmpty()) qw.eq(ReleaseUnitRelation::getProtocol, protocol);
        qw.orderByDesc(ReleaseUnitRelation::getCreatedAt);
        Page<ReleaseUnitRelation> res = relationMapper.selectPage(p, qw);
        Page<ReleaseUnitRelationVO> voPage = new Page<>();
        BeanUtils.copyProperties(res, voPage, "records");
        voPage.setRecords(res.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public ReleaseUnitRelationVO detail(Long id) {
        ReleaseUnitRelation r = relationMapper.selectById(id);
        if (r == null) throw new BusinessException("调用关系不存在");
        return toVO(r);
    }

    @Override
    public void create(ReleaseUnitRelationDTO dto) {
        long count = relationMapper.selectCount(new LambdaQueryWrapper<ReleaseUnitRelation>()
            .eq(ReleaseUnitRelation::getDeploySysNo, dto.getDeploySysNo())
            .eq(ReleaseUnitRelation::getCallerUnitNo, dto.getCallerUnitNo())
            .eq(ReleaseUnitRelation::getCalleeUnitNo, dto.getCalleeUnitNo())
            .isNull(ReleaseUnitRelation::getDeletedAt));
        if (count > 0) throw new BusinessException("同一部署子系统下该调用关系已存在");
        ReleaseUnitRelation r = new ReleaseUnitRelation();
        BeanUtils.copyProperties(dto, r);
        relationMapper.insert(r);
    }

    @Override
    public void update(Long id, ReleaseUnitRelationDTO dto) {
        ReleaseUnitRelation r = relationMapper.selectById(id);
        if (r == null) throw new BusinessException("调用关系不存在");
        long count = relationMapper.selectCount(new LambdaQueryWrapper<ReleaseUnitRelation>()
            .eq(ReleaseUnitRelation::getDeploySysNo, dto.getDeploySysNo())
            .eq(ReleaseUnitRelation::getCallerUnitNo, dto.getCallerUnitNo())
            .eq(ReleaseUnitRelation::getCalleeUnitNo, dto.getCalleeUnitNo())
            .ne(ReleaseUnitRelation::getId, id)
            .isNull(ReleaseUnitRelation::getDeletedAt));
        if (count > 0) throw new BusinessException("同一部署子系统下该调用关系已存在");
        BeanUtils.copyProperties(dto, r, "id");
        relationMapper.updateById(r);
    }

    @Override
    public void delete(Long id) {
        relationMapper.deleteById(id);
    }

    private ReleaseUnitRelationVO toVO(ReleaseUnitRelation r) {
        ReleaseUnitRelationVO vo = new ReleaseUnitRelationVO();
        BeanUtils.copyProperties(r, vo);
        return vo;
    }
}
