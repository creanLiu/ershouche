package com.usedcar.module.vehicle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.R;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.vehicle.entity.BrowseHistory;
import com.usedcar.module.vehicle.entity.Vehicle;
import com.usedcar.module.vehicle.mapper.BrowseHistoryMapper;
import com.usedcar.module.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "浏览历史-小程序", description = "小程序端浏览历史接口")
@RestController
@RequestMapping("/api/v1/mp/browse-history")
@RequiredArgsConstructor
public class MpBrowseHistoryController {

    private final BrowseHistoryMapper browseHistoryMapper;
    private final VehicleMapper vehicleMapper;

    @Operation(summary = "浏览历史列表（按日期分组）")
    @GetMapping
    public R<Map<String, List<Map<String, Object>>>> list() {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);

        List<BrowseHistory> histories = browseHistoryMapper.selectList(
                new LambdaQueryWrapper<BrowseHistory>()
                        .eq(BrowseHistory::getWxUserId, wxUserId)
                        .orderByDesc(BrowseHistory::getCreatedAt)
                        .last("LIMIT 100"));

        if (histories.isEmpty()) return R.ok(Map.of());

        List<Long> vehicleIds = histories.stream().map(BrowseHistory::getVehicleId).distinct().toList();
        Map<Long, Vehicle> vehicleMap = vehicleMapper.selectBatchIds(vehicleIds)
                .stream().collect(Collectors.toMap(Vehicle::getId, v -> v));

        // 按日期分组
        Map<String, List<Map<String, Object>>> grouped = new LinkedHashMap<>();
        for (BrowseHistory h : histories) {
            String date = h.getCreatedAt().toLocalDate().toString();
            Vehicle v = vehicleMap.get(h.getVehicleId());
            if (v == null) continue;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("vehicleId", v.getId());
            item.put("coverImageUrl", v.getCoverImageUrl());
            item.put("price", v.getPrice());
            item.put("mileage", v.getMileage());
            item.put("browseTime", h.getCreatedAt().toString());
            grouped.computeIfAbsent(date, k -> new java.util.ArrayList<>()).add(item);
        }
        return R.ok(grouped);
    }

    @Operation(summary = "记录浏览历史")
    @PostMapping("/{vehicleId}")
    public R<Void> record(@PathVariable Long vehicleId) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) return R.ok(); // 未登录不记录

        BrowseHistory history = new BrowseHistory();
        history.setWxUserId(wxUserId);
        history.setVehicleId(vehicleId);
        history.setCreatedAt(LocalDateTime.now());
        browseHistoryMapper.insert(history);
        return R.ok();
    }
}
