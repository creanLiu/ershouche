package com.usedcar.module.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "车辆创建/更新请求")
public class VehicleSaveReq {

    @Schema(description = "VIN码")
    private String vin;

    @NotNull(message = "品牌不能为空")
    @Schema(description = "品牌ID")
    private Long brandId;

    @NotNull(message = "车型不能为空")
    @Schema(description = "车型ID")
    private Long modelId;

    @NotNull(message = "年份不能为空")
    @Schema(description = "年份")
    private Integer year;

    @NotNull(message = "价格不能为空")
    @Schema(description = "售价(元)")
    private BigDecimal price;

    @Schema(description = "原价(元)")
    private BigDecimal originalPrice;

    @NotNull(message = "里程不能为空")
    @Schema(description = "里程(km)")
    private Integer mileage;

    @Schema(description = "排量")
    private String displacement;

    @Schema(description = "变速箱: 1=自动 2=手动 3=CVT 4=双离合")
    private Integer transmission;

    @Schema(description = "燃油类型: 1=汽油 2=柴油 3=混动 4=纯电")
    private Integer fuelType;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "内饰颜色")
    private String interiorColor;

    @Schema(description = "座位数")
    private Integer seats;

    @Schema(description = "排放标准")
    private String emissionStandard;

    @Schema(description = "上牌日期")
    private LocalDate registrationDate;

    @Schema(description = "上牌城市")
    private String registrationCity;

    @Schema(description = "年检日期")
    private LocalDate inspectionDate;

    @Schema(description = "保险到期日")
    private LocalDate insuranceDate;

    @Schema(description = "车辆描述")
    private String description;

    @Schema(description = "封面图URL")
    private String coverImageUrl;
}
