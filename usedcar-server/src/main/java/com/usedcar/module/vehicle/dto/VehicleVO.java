package com.usedcar.module.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "车辆列表项")
public class VehicleVO {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String color;
    private String transmission;
    private String city;
    private String coverImageUrl;
    @Schema(description = "状态: 1=草稿 2=在售 3=下架 4=已售")
    private Integer status;
    private Integer viewCount;
    private Integer favoriteCount;
}
