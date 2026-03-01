package com.usedcar.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usedcar.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    private Long parentId;
    private String permCode;
    private String permName;
    private Integer permType;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Integer status;
}
