package com.usedcar.module.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wx_user")
public class WxUser extends BaseEntity {

    private String openid;
    private String unionid;
    private String phone;
    private String nickname;
    private String avatarUrl;
    private Integer gender;
    private String city;
    private String province;
    private LocalDateTime lastLoginAt;
    private Integer status;

    @TableLogic
    private Integer deleted;
}
