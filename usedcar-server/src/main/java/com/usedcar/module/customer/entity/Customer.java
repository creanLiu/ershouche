package com.usedcar.module.customer.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {

    private Long wxUserId;
    private String name;
    private String phone;
    private String wechatId;
    private Integer gender;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String preferredBrands;
    private String preferredTypes;
    private Integer intentLevel;
    private Integer stage;
    private String source;
    private Long assignedTo;
    private LocalDateTime nextFollowupAt;
    private String remark;

    @TableLogic
    private Integer deleted;
}
