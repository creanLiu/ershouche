package com.usedcar.module.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.ResultCode;
import com.usedcar.module.vehicle.dto.*;
import com.usedcar.module.vehicle.entity.Brand;
import com.usedcar.module.vehicle.entity.CarModel;
import com.usedcar.module.vehicle.entity.Vehicle;
import com.usedcar.module.vehicle.entity.VehicleImage;
import com.usedcar.module.vehicle.mapper.BrandMapper;
import com.usedcar.module.vehicle.mapper.CarModelMapper;
import com.usedcar.module.vehicle.mapper.VehicleImageMapper;
import com.usedcar.module.vehicle.mapper.VehicleMapper;
import com.usedcar.module.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    private final BrandMapper brandMapper;
    private final CarModelMapper carModelMapper;
    private final VehicleImageMapper vehicleImageMapper;

    private static final Map<Integer, String> TRANSMISSION_MAP = Map.of(
            1, "自动", 2, "手动", 3, "CVT", 4, "双离合");
    private static final Map<Integer, String> FUEL_MAP = Map.of(
            1, "汽油", 2, "柴油", 3, "混动", 4, "纯电");

    @Override
    public Page<VehicleVO> listVehicles(VehicleQueryReq req) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<Vehicle>()
                .eq(Vehicle::getStatus, 2); // 只查在售

        if (req.getBrandId() != null) wrapper.eq(Vehicle::getBrandId, req.getBrandId());
        if (req.getModelId() != null) wrapper.eq(Vehicle::getModelId, req.getModelId());
        if (req.getMinPrice() != null) wrapper.ge(Vehicle::getPrice, req.getMinPrice());
        if (req.getMaxPrice() != null) wrapper.le(Vehicle::getPrice, req.getMaxPrice());
        if (req.getMinMileage() != null) wrapper.ge(Vehicle::getMileage, req.getMinMileage());
        if (req.getMaxMileage() != null) wrapper.le(Vehicle::getMileage, req.getMaxMileage());
        if (req.getMinYear() != null) wrapper.ge(Vehicle::getYear, req.getMinYear());
        if (req.getMaxYear() != null) wrapper.le(Vehicle::getYear, req.getMaxYear());
        if (req.getTransmission() != null) wrapper.eq(Vehicle::getTransmission, req.getTransmission());
        if (StringUtils.hasText(req.getCity())) wrapper.eq(Vehicle::getRegistrationCity, req.getCity());

        switch (req.getSortBy() == null ? "newest" : req.getSortBy()) {
            case "price_asc" -> wrapper.orderByAsc(Vehicle::getPrice);
            case "price_desc" -> wrapper.orderByDesc(Vehicle::getPrice);
            case "mileage_asc" -> wrapper.orderByAsc(Vehicle::getMileage);
            default -> wrapper.orderByDesc(Vehicle::getCreatedAt);
        }

        Page<Vehicle> page = this.page(new Page<>(req.getPage(), req.getSize()), wrapper);

        // 批量查品牌/车型名称
        List<Long> brandIds = page.getRecords().stream().map(Vehicle::getBrandId).distinct().toList();
        List<Long> modelIds = page.getRecords().stream().map(Vehicle::getModelId).distinct().toList();
        Map<Long, String> brandNames = brandIds.isEmpty() ? Map.of() :
                brandMapper.selectBatchIds(brandIds).stream().collect(Collectors.toMap(Brand::getId, Brand::getName));
        Map<Long, String> modelNames = modelIds.isEmpty() ? Map.of() :
                carModelMapper.selectBatchIds(modelIds).stream().collect(Collectors.toMap(CarModel::getId, CarModel::getName));

        Page<VehicleVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(v -> toVO(v, brandNames, modelNames)).toList());
        return result;
    }

    @Override
    public VehicleDetailVO getVehicleDetail(Long id, Long wxUserId) {
        Vehicle vehicle = this.getById(id);
        if (vehicle == null) throw new BusinessException(ResultCode.VEHICLE_NOT_FOUND);

        Brand brand = brandMapper.selectById(vehicle.getBrandId());
        CarModel model = carModelMapper.selectById(vehicle.getModelId());

        List<VehicleImage> images = vehicleImageMapper.selectList(
                new LambdaQueryWrapper<VehicleImage>()
                        .eq(VehicleImage::getVehicleId, id)
                        .orderByAsc(VehicleImage::getSortOrder));

        // increment view count
        vehicle.setViewCount(vehicle.getViewCount() == null ? 1 : vehicle.getViewCount() + 1);
        this.updateById(vehicle);

        VehicleDetailVO vo = new VehicleDetailVO();
        vo.setId(vehicle.getId());
        vo.setVin(vehicle.getVin());
        vo.setBrandId(vehicle.getBrandId());
        vo.setBrand(brand != null ? brand.getName() : "");
        vo.setModelId(vehicle.getModelId());
        vo.setModel(model != null ? model.getName() : "");
        vo.setYear(vehicle.getYear());
        vo.setPrice(vehicle.getPrice());
        vo.setOriginalPrice(vehicle.getOriginalPrice());
        vo.setMileage(vehicle.getMileage());
        vo.setDisplacement(vehicle.getDisplacement());
        vo.setTransmission(vehicle.getTransmission());
        vo.setTransmissionName(TRANSMISSION_MAP.getOrDefault(vehicle.getTransmission(), ""));
        vo.setFuelType(vehicle.getFuelType());
        vo.setFuelTypeName(FUEL_MAP.getOrDefault(vehicle.getFuelType(), ""));
        vo.setColor(vehicle.getColor());
        vo.setInteriorColor(vehicle.getInteriorColor());
        vo.setSeats(vehicle.getSeats());
        vo.setEmissionStandard(vehicle.getEmissionStandard());
        vo.setRegistrationDate(vehicle.getRegistrationDate());
        vo.setRegistrationCity(vehicle.getRegistrationCity());
        vo.setInspectionDate(vehicle.getInspectionDate());
        vo.setInsuranceDate(vehicle.getInsuranceDate());
        vo.setDescription(vehicle.getDescription());
        vo.setCoverImageUrl(vehicle.getCoverImageUrl());
        vo.setStatus(vehicle.getStatus());
        vo.setViewCount(vehicle.getViewCount());
        vo.setFavoriteCount(vehicle.getFavoriteCount());
        vo.setImages(images.stream().map(VehicleImage::getImageUrl).toList());
        vo.setIsFavorite(false); // 收藏状态在 MpFavoriteController 中处理
        return vo;
    }

    @Override
    public Long createVehicle(VehicleSaveReq req, Long sellerId) {
        Vehicle vehicle = new Vehicle();
        copyFromReq(req, vehicle);
        vehicle.setSellerId(sellerId);
        vehicle.setStatus(1); // 草稿
        vehicle.setViewCount(0);
        vehicle.setFavoriteCount(0);
        this.save(vehicle);
        return vehicle.getId();
    }

    @Override
    public void updateVehicle(Long id, VehicleSaveReq req) {
        Vehicle vehicle = this.getById(id);
        if (vehicle == null) throw new BusinessException(ResultCode.VEHICLE_NOT_FOUND);
        copyFromReq(req, vehicle);
        this.updateById(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = this.getById(id);
        if (vehicle == null) throw new BusinessException(ResultCode.VEHICLE_NOT_FOUND);
        this.removeById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Vehicle vehicle = this.getById(id);
        if (vehicle == null) throw new BusinessException(ResultCode.VEHICLE_NOT_FOUND);
        vehicle.setStatus(status);
        this.updateById(vehicle);
    }

    private VehicleVO toVO(Vehicle v, Map<Long, String> brandNames, Map<Long, String> modelNames) {
        VehicleVO vo = new VehicleVO();
        vo.setId(v.getId());
        vo.setBrand(brandNames.getOrDefault(v.getBrandId(), ""));
        vo.setModel(modelNames.getOrDefault(v.getModelId(), ""));
        vo.setYear(v.getYear());
        vo.setPrice(v.getPrice());
        vo.setMileage(v.getMileage());
        vo.setColor(v.getColor());
        vo.setTransmission(TRANSMISSION_MAP.getOrDefault(v.getTransmission(), ""));
        vo.setCity(v.getRegistrationCity());
        vo.setCoverImageUrl(v.getCoverImageUrl());
        vo.setStatus(v.getStatus());
        vo.setViewCount(v.getViewCount());
        vo.setFavoriteCount(v.getFavoriteCount());
        return vo;
    }

    private void copyFromReq(VehicleSaveReq req, Vehicle vehicle) {
        vehicle.setVin(req.getVin());
        vehicle.setBrandId(req.getBrandId());
        vehicle.setModelId(req.getModelId());
        vehicle.setYear(req.getYear());
        vehicle.setPrice(req.getPrice());
        vehicle.setOriginalPrice(req.getOriginalPrice());
        vehicle.setMileage(req.getMileage());
        vehicle.setDisplacement(req.getDisplacement());
        vehicle.setTransmission(req.getTransmission());
        vehicle.setFuelType(req.getFuelType());
        vehicle.setColor(req.getColor());
        vehicle.setInteriorColor(req.getInteriorColor());
        vehicle.setSeats(req.getSeats());
        vehicle.setEmissionStandard(req.getEmissionStandard());
        vehicle.setRegistrationDate(req.getRegistrationDate());
        vehicle.setRegistrationCity(req.getRegistrationCity());
        vehicle.setInspectionDate(req.getInspectionDate());
        vehicle.setInsuranceDate(req.getInsuranceDate());
        vehicle.setDescription(req.getDescription());
        vehicle.setCoverImageUrl(req.getCoverImageUrl());
    }
}
