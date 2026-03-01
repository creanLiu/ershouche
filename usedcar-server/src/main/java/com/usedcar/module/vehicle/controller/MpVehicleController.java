package com.usedcar.module.vehicle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.vehicle.dto.*;
import com.usedcar.module.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "车辆信息-小程序", description = "小程序端车辆浏览接口")
@RestController
@RequestMapping("/api/v1/mp/vehicles")
@RequiredArgsConstructor
public class MpVehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "测试接口-返回假车辆数据")
    @GetMapping("/test")
    public R<Map<String, Object>> test() {
        Map<String, Object> vehicle = new java.util.LinkedHashMap<>();
        vehicle.put("id", 1);
        vehicle.put("brand", "丰田");
        vehicle.put("model", "凯美瑞");
        vehicle.put("year", 2021);
        vehicle.put("price", 168000);
        vehicle.put("mileage", 32000);
        vehicle.put("color", "珍珠白");
        vehicle.put("transmission", "自动");
        vehicle.put("displacement", "2.0L");
        vehicle.put("city", "上海");
        vehicle.put("status", 1);
        vehicle.put("coverImageUrl", "https://via.placeholder.com/400x300?text=Toyota+Camry");
        return R.ok(vehicle);
    }

    @Operation(summary = "车辆列表(含筛选)")
    @GetMapping
    public R<Page<VehicleVO>> list(VehicleQueryReq req) {
        return R.ok(vehicleService.listVehicles(req));
    }

    @Operation(summary = "车辆详情")
    @GetMapping("/{id}")
    public R<VehicleDetailVO> detail(@PathVariable Long id) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        return R.ok(vehicleService.getVehicleDetail(id, wxUserId));
    }
}
