package com.usedcar.module.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("brand")
public class Brand extends BaseEntity {

    private String name;
    private String nameEn;
    private String logoUrl;
    private String firstLetter;
    private Integer sortOrder;
    private Integer status;
}
