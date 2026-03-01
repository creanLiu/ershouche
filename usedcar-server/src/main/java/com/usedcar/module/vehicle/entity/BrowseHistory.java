package com.usedcar.module.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("browse_history")
public class BrowseHistory implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long wxUserId;
    private Long vehicleId;
    private Integer duration;
    private LocalDateTime createdAt;
}
