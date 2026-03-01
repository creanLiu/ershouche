package com.usedcar.module.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vehicle")
public class Vehicle extends BaseEntity {

    private String vin;
    private Long brandId;
    private Long modelId;
    private Integer year;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer mileage;
    private String displacement;
    private Integer transmission;
    private Integer fuelType;
    private String color;
    private String interiorColor;
    private Integer seats;
    private String emissionStandard;
    private LocalDate registrationDate;
    private String registrationCity;
    private LocalDate inspectionDate;
    private LocalDate insuranceDate;
    private String description;
    private String coverImageUrl;
    private Integer status;
    private Long sellerId;
    private Integer viewCount;
    private Integer favoriteCount;

    @TableLogic
    private Integer deleted;
}
