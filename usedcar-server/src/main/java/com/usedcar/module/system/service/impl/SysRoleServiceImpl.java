package com.usedcar.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.module.system.entity.SysRole;
import com.usedcar.module.system.mapper.SysRoleMapper;
import com.usedcar.module.system.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
