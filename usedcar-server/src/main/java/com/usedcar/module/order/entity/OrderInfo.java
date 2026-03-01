package com.usedcar.module.order.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_info")
public class OrderInfo extends BaseEntity {

    private String orderNo;
    private Long vehicleId;
    private Long buyerId;
    private Long sellerId;
    private BigDecimal dealPrice;
    private BigDecimal deposit;
    private Integer status;
    private Integer paymentMethod;
    private String remark;
    private LocalDateTime dealAt;

    @TableLogic
    private Integer deleted;
}
