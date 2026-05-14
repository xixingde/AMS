package com.cmhk.ams.service.impl;

import com.cmhk.ams.model.DiagramLayout;
import com.cmhk.ams.model.ReleaseUnit;
import com.cmhk.ams.model.ReleaseUnitRelation;
import com.cmhk.ams.mapper.DiagramLayoutMapper;
import com.cmhk.ams.mapper.ReleaseUnitMapper;
import com.cmhk.ams.mapper.ReleaseUnitRelationMapper;
import com.cmhk.ams.service.DiagramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagramServiceImpl implements DiagramService {

    private final ReleaseUnitMapper releaseUnitMapper;
    private final ReleaseUnitRelationMapper relationMapper;
    private final DiagramLayoutMapper diagramLayoutMapper;

    @Override
    public Map<String, Object> getDiagram(String deploySysNo) {
        List<ReleaseUnit> units = releaseUnitMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ReleaseUnit>()
                .eq(ReleaseUnit::getDeploySysNo, deploySysNo)
                .isNull(ReleaseUnit::getDeletedAt)
        );
        List<ReleaseUnitRelation> relations = relationMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ReleaseUnitRelation>()
                .eq(ReleaseUnitRelation::getDeploySysNo, deploySysNo)
                .isNull(ReleaseUnitRelation::getDeletedAt)
        );
        String mermaid = generateMermaid(units, relations);
        Map<String, Object> result = new HashMap<>();
        result.put("deploySysNo", deploySysNo);
        result.put("mermaid", mermaid);
        result.put("unitCount", units.size());
        if (units.isEmpty()) {
            result.put("empty", true);
            result.put("message", "当前部署子系统下暂无发布单元，无法生成架构图");
        }
        return result;
    }

    @Override
    public void saveLayout(String deploySysNo, String layoutData, String remark) {
        DiagramLayout layout = diagramLayoutMapper.selectByDeploySysNo(deploySysNo);
        if (layout == null) {
            layout = new DiagramLayout();
            layout.setDeploySysNo(deploySysNo);
        }
        layout.setLayoutData(layoutData);
        layout.setRemark(remark);
        if (layout.getId() == null) {
            diagramLayoutMapper.insert(layout);
        } else {
            diagramLayoutMapper.updateById(layout);
        }
    }

    @Override
    public Map<String, Object> getLayout(String deploySysNo) {
        DiagramLayout layout = diagramLayoutMapper.selectByDeploySysNo(deploySysNo);
        Map<String, Object> result = new HashMap<>();
        if (layout != null) {
            result.put("layoutData", layout.getLayoutData());
            result.put("remark", layout.getRemark());
        }
        return result;
    }

    private String generateMermaid(List<ReleaseUnit> units, List<ReleaseUnitRelation> relations) {
        if (units.isEmpty()) {
            return "graph LR\n    A[暂无数据]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("graph LR\n");
        Map<String, List<ReleaseUnit>> zoneMap = units.stream().collect(Collectors.groupingBy(ReleaseUnit::getNetworkZone));
        int sgIndex = 0;
        for (Map.Entry<String, List<ReleaseUnit>> entry : zoneMap.entrySet()) {
            sb.append("    subgraph ").append(entry.getKey()).append("\n");
            for (ReleaseUnit u : entry.getValue()) {
                sb.append("        ").append(u.getUnitNo().replace("-", "_"))
                  .append("[").append(u.getUnitNameCn()).append("]\n");
            }
            sb.append("    end\n");
        }
        for (ReleaseUnitRelation r : relations) {
            String caller = r.getCallerUnitNo().replace("-", "_");
            String callee = r.getCalleeUnitNo().replace("-", "_");
            sb.append("    ").append(caller).append(" -->|").append(r.getProtocol()).append("| ").append(callee).append("\n");
        }
        sb.append("    classDef current fill:#90EE90,stroke:#333,stroke-width:1px;\n");
        sb.append("    classDef other fill:#E6F7E6,stroke:#333,stroke-width:1px;\n");
        for (ReleaseUnit u : units) {
            sb.append("    class ").append(u.getUnitNo().replace("-", "_")).append(" current;\n");
        }
        return sb.toString();
    }
}
