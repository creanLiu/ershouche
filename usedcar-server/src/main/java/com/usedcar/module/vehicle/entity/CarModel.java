package com.usedcar.module.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("car_model")
public class CarModel extends BaseEntity {

    private Long brandId;
    private String name;
    private String category;
    private Integer sortOrder;
    private Integer status;
}
