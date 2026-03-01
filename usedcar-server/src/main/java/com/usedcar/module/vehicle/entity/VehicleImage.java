package com.usedcar.module.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("vehicle_image")
public class VehicleImage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private String imageUrl;
    private String thumbUrl;
    private Integer imageType;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
