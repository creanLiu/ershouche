package com.usedcar.module.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("customer_followup")
public class CustomerFollowup implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long operatorId;
    private String content;
    private Integer followupType;
    private String result;
    private LocalDateTime nextFollowupAt;
    private LocalDateTime createdAt;
}
