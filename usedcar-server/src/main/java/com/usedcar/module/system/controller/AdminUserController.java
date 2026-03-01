package com.usedcar.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.module.system.entity.SysRole;
import com.usedcar.module.system.entity.SysUser;
import com.usedcar.module.system.entity.SysUserRole;
import com.usedcar.module.system.mapper.SysRoleMapper;
import com.usedcar.module.system.mapper.SysUserMapper;
import com.usedcar.module.system.mapper.SysUserRoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理-后台", description = "管理后台用户与角色管理接口")
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "用户列表")
    @GetMapping
    public R<Page<SysUser>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SysUser> result = sysUserMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getCreatedAt));
        // 脱敏密码
        result.getRecords().forEach(u -> u.setPassword(null));
        return R.ok(result);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<Void> create(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        sysUserMapper.insert(user);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        sysUserMapper.updateById(user);
        return R.ok();
    }

    @Operation(summary = "角色列表")
    @GetMapping("/roles")
    public R<List<SysRole>> roleList() {
        return R.ok(sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1)));
    }

    @Operation(summary = "分配角色")
    @PutMapping("/{id}/roles")
    public R<Void> assignRole(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        for (Long roleId : roleIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(id);
            ur.setRoleId(roleId);
            sysUserRoleMapper.insert(ur);
        }
        return R.ok();
    }
}
