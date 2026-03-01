package com.usedcar.module.search.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.module.vehicle.dto.VehicleQueryReq;
import com.usedcar.module.vehicle.dto.VehicleVO;
import com.usedcar.module.vehicle.entity.Brand;
import com.usedcar.module.vehicle.mapper.BrandMapper;
import com.usedcar.module.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "搜索-小程序", description = "小程序端搜索接口")
@RestController
@RequestMapping("/api/v1/mp/search")
@RequiredArgsConstructor
public class MpSearchController {

    private final VehicleService vehicleService;
    private final BrandMapper brandMapper;

    @Operation(summary = "关键词搜索车辆")
    @GetMapping
    public R<Page<VehicleVO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        VehicleQueryReq req = new VehicleQueryReq();
        req.setPage(page);
        req.setSize(size);
        return R.ok(vehicleService.listVehicles(req));
    }

    @Operation(summary = "搜索建议/联想（品牌名）")
    @GetMapping("/suggest")
    public R<List<String>> suggest(@RequestParam String keyword) {
        if (!StringUtils.hasText(keyword)) return R.ok(List.of());
        List<Brand> brands = brandMapper.selectList(
                new LambdaQueryWrapper<Brand>()
                        .like(Brand::getName, keyword)
                        .eq(Brand::getStatus, 1)
                        .last("LIMIT 10"));
        return R.ok(brands.stream().map(Brand::getName).toList());
    }

    @Operation(summary = "热门搜索词")
    @GetMapping("/hot")
    public R<List<String>> hotKeywords() {
        return R.ok(List.of("宝马", "奔驰", "丰田", "本田", "大众", "特斯拉", "奥迪", "日产"));
    }
}
