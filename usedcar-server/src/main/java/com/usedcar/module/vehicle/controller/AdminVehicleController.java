package com.usedcar.module.vehicle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.vehicle.dto.*;
import com.usedcar.module.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "车辆管理-后台", description = "管理后台车辆管理接口")
@RestController
@RequestMapping("/api/v1/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "车辆列表")
    @GetMapping
    public R<Page<VehicleVO>> list(VehicleQueryReq req) {
        // 管理端可查所有状态，不强制 status=2
        return R.ok(vehicleService.listVehicles(req));
    }

    @Operation(summary = "车辆详情")
    @GetMapping("/{id}")
    public R<VehicleDetailVO> detail(@PathVariable Long id) {
        return R.ok(vehicleService.getVehicleDetail(id, null));
    }

    @Operation(summary = "新增车辆")
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES')")
    public R<Long> create(@Valid @RequestBody VehicleSaveReq req) {
        Long sellerId = SecurityUtils.getCurrentUserId();
        return R.ok(vehicleService.createVehicle(req, sellerId));
    }

    @Operation(summary = "更新车辆")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES')")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody VehicleSaveReq req) {
        vehicleService.updateVehicle(id, req);
        return R.ok();
    }

    @Operation(summary = "删除车辆")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public R<Void> delete(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return R.ok();
    }

    @Operation(summary = "更新车辆状态(上架/下架)")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES')")
    public R<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        vehicleService.updateStatus(id, status);
        return R.ok();
    }
}
