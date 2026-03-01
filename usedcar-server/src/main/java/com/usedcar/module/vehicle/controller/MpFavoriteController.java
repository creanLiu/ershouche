package com.usedcar.module.vehicle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.R;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.vehicle.dto.VehicleVO;
import com.usedcar.module.vehicle.entity.Favorite;
import com.usedcar.module.vehicle.entity.Vehicle;
import com.usedcar.module.vehicle.mapper.FavoriteMapper;
import com.usedcar.module.vehicle.mapper.VehicleMapper;
import com.usedcar.module.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "收藏-小程序", description = "小程序端收藏接口")
@RestController
@RequestMapping("/api/v1/mp/favorites")
@RequiredArgsConstructor
public class MpFavoriteController {

    private final FavoriteMapper favoriteMapper;
    private final VehicleMapper vehicleMapper;
    private final VehicleService vehicleService;

    @Operation(summary = "收藏列表")
    @GetMapping
    public R<List<VehicleVO>> list() {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);

        List<Favorite> favorites = favoriteMapper.selectList(
                new LambdaQueryWrapper<Favorite>().eq(Favorite::getWxUserId, wxUserId)
                        .orderByDesc(Favorite::getCreatedAt));

        if (favorites.isEmpty()) return R.ok(List.of());

        List<Long> vehicleIds = favorites.stream().map(Favorite::getVehicleId).toList();
        List<Vehicle> vehicles = vehicleMapper.selectBatchIds(vehicleIds);
        Map<Long, Vehicle> vehicleMap = vehicles.stream().collect(Collectors.toMap(Vehicle::getId, v -> v));

        List<VehicleVO> result = vehicleIds.stream()
                .filter(vehicleMap::containsKey)
                .map(id -> {
                    Vehicle v = vehicleMap.get(id);
                    VehicleVO vo = new VehicleVO();
                    vo.setId(v.getId());
                    vo.setYear(v.getYear());
                    vo.setPrice(v.getPrice());
                    vo.setMileage(v.getMileage());
                    vo.setColor(v.getColor());
                    vo.setCity(v.getRegistrationCity());
                    vo.setCoverImageUrl(v.getCoverImageUrl());
                    vo.setStatus(v.getStatus());
                    return vo;
                }).toList();

        return R.ok(result);
    }

    @Operation(summary = "添加收藏")
    @PostMapping("/{vehicleId}")
    public R<Void> add(@PathVariable Long vehicleId) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);

        long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getWxUserId, wxUserId)
                        .eq(Favorite::getVehicleId, vehicleId));
        if (count > 0) return R.ok();

        Favorite favorite = new Favorite();
        favorite.setWxUserId(wxUserId);
        favorite.setVehicleId(vehicleId);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteMapper.insert(favorite);

        // 更新收藏数
        Vehicle vehicle = vehicleMapper.selectById(vehicleId);
        if (vehicle != null) {
            vehicle.setFavoriteCount(vehicle.getFavoriteCount() == null ? 1 : vehicle.getFavoriteCount() + 1);
            vehicleMapper.updateById(vehicle);
        }
        return R.ok();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{vehicleId}")
    public R<Void> remove(@PathVariable Long vehicleId) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);

        favoriteMapper.delete(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getWxUserId, wxUserId)
                        .eq(Favorite::getVehicleId, vehicleId));

        Vehicle vehicle = vehicleMapper.selectById(vehicleId);
        if (vehicle != null && vehicle.getFavoriteCount() != null && vehicle.getFavoriteCount() > 0) {
            vehicle.setFavoriteCount(vehicle.getFavoriteCount() - 1);
            vehicleMapper.updateById(vehicle);
        }
        return R.ok();
    }
}
