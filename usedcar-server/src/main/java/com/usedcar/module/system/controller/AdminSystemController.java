package com.usedcar.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.module.system.entity.OperationLog;
import com.usedcar.module.system.entity.SysConfig;
import com.usedcar.module.system.mapper.OperationLogMapper;
import com.usedcar.module.system.mapper.SysConfigMapper;
import com.usedcar.module.vehicle.entity.Brand;
import com.usedcar.module.vehicle.entity.CarModel;
import com.usedcar.module.vehicle.mapper.BrandMapper;
import com.usedcar.module.vehicle.mapper.CarModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "系统管理-后台", description = "管理后台系统管理接口")
@RestController
@RequestMapping("/api/v1/admin/system")
@RequiredArgsConstructor
public class AdminSystemController {

    private final BrandMapper brandMapper;
    private final CarModelMapper carModelMapper;
    private final SysConfigMapper sysConfigMapper;
    private final OperationLogMapper operationLogMapper;

    @Operation(summary = "品牌列表")
    @GetMapping("/brands")
    public R<List<Brand>> brandList() {
        return R.ok(brandMapper.selectList(
                new LambdaQueryWrapper<Brand>().orderByAsc(Brand::getSortOrder)));
    }

    @Operation(summary = "新增品牌")
    @PostMapping("/brands")
    public R<Void> createBrand(@Valid @RequestBody Brand brand) {
        brandMapper.insert(brand);
        return R.ok();
    }

    @Operation(summary = "更新品牌")
    @PutMapping("/brands/{id}")
    public R<Void> updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        brand.setId(id);
        brandMapper.updateById(brand);
        return R.ok();
    }

    @Operation(summary = "车型列表")
    @GetMapping("/brands/{brandId}/models")
    public R<List<CarModel>> modelList(@PathVariable Long brandId) {
        return R.ok(carModelMapper.selectList(
                new LambdaQueryWrapper<CarModel>()
                        .eq(CarModel::getBrandId, brandId)
                        .orderByAsc(CarModel::getSortOrder)));
    }

    @Operation(summary = "新增车型")
    @PostMapping("/brands/{brandId}/models")
    public R<Void> createModel(@PathVariable Long brandId, @RequestBody CarModel model) {
        model.setBrandId(brandId);
        carModelMapper.insert(model);
        return R.ok();
    }

    @Operation(summary = "操作日志列表")
    @GetMapping("/operation-logs")
    public R<Page<OperationLog>> operationLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(operationLogMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<OperationLog>().orderByDesc(OperationLog::getCreatedAt)));
    }

    @Operation(summary = "系统配置列表")
    @GetMapping("/configs")
    public R<List<SysConfig>> configList() {
        return R.ok(sysConfigMapper.selectList(null));
    }

    @Operation(summary = "更新系统配置")
    @PutMapping("/configs/{key}")
    public R<Void> updateConfig(@PathVariable String key, @RequestBody Map<String, String> body) {
        SysConfig config = sysConfigMapper.selectOne(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key));
        if (config != null) {
            config.setConfigValue(body.get("value"));
            sysConfigMapper.updateById(config);
        }
        return R.ok();
    }
}
