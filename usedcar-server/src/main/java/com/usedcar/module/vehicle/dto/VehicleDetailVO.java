package com.usedcar.module.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "车辆详情")
public class VehicleDetailVO {

    private Long id;
    private String vin;
    private Long brandId;
    private String brand;
    private Long modelId;
    private String model;
    private Integer year;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer mileage;
    private String displacement;
    private Integer transmission;
    private String transmissionName;
    private Integer fuelType;
    private String fuelTypeName;
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
    private Integer viewCount;
    private Integer favoriteCount;
    private List<String> images;
    private Boolean isFavorite;
}
