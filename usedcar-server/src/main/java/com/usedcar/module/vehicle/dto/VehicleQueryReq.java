package com.usedcar.module.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车辆列表查询参数")
public class VehicleQueryReq {

    @Schema(description = "品牌ID")
    private Long brandId;

    @Schema(description = "车型ID")
    private Long modelId;

    @Schema(description = "最低价格(元)")
    private Integer minPrice;

    @Schema(description = "最高价格(元)")
    private Integer maxPrice;

    @Schema(description = "最低里程(km)")
    private Integer minMileage;

    @Schema(description = "最高里程(km)")
    private Integer maxMileage;

    @Schema(description = "最小年份")
    private Integer minYear;

    @Schema(description = "最大年份")
    private Integer maxYear;

    @Schema(description = "变速箱: 1=自动 2=手动 3=CVT 4=双离合")
    private Integer transmission;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "排序: newest/price_asc/price_desc/mileage_asc", example = "newest")
    private String sortBy = "newest";

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer size = 10;
}
