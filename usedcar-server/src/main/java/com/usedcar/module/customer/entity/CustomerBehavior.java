package com.usedcar.module.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("customer_behavior")
public class CustomerBehavior implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long wxUserId;
    private String eventType;
    private String targetType;
    private String targetId;
    private String extraData;
    private LocalDateTime createdAt;
}
