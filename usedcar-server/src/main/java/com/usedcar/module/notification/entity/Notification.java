package com.usedcar.module.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long wxUserId;
    private String title;
    private String content;
    private Integer notifyType;
    private String targetType;
    private Long targetId;
    private Integer isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
