package com.usedcar.module.vehicle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usedcar.module.vehicle.dto.VehicleDetailVO;
import com.usedcar.module.vehicle.dto.VehicleQueryReq;
import com.usedcar.module.vehicle.dto.VehicleSaveReq;
import com.usedcar.module.vehicle.dto.VehicleVO;
import com.usedcar.module.vehicle.entity.Vehicle;

public interface VehicleService extends IService<Vehicle> {

    Page<VehicleVO> listVehicles(VehicleQueryReq req);

    VehicleDetailVO getVehicleDetail(Long id, Long wxUserId);

    Long createVehicle(VehicleSaveReq req, Long sellerId);

    void updateVehicle(Long id, VehicleSaveReq req);

    void deleteVehicle(Long id);

    void updateStatus(Long id, Integer status);
}
