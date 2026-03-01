package com.usedcar.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.util.JwtUtil;
import com.usedcar.module.auth.dto.AdminLoginReq;
import com.usedcar.module.auth.dto.AdminLoginResp;
import com.usedcar.module.auth.dto.TokenResp;
import com.usedcar.module.auth.service.AdminAuthService;
import com.usedcar.module.system.entity.SysPermission;
import com.usedcar.module.system.entity.SysRole;
import com.usedcar.module.system.entity.SysUser;
import com.usedcar.module.system.mapper.SysPermissionMapper;
import com.usedcar.module.system.mapper.SysRoleMapper;
import com.usedcar.module.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AdminLoginResp login(AdminLoginReq req) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername()));
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        // update last login time
        user.setLastLoginAt(LocalDateTime.now());
        sysUserMapper.updateById(user);

        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(SysRole::getRoleCode).toList();

        List<SysPermission> perms = sysPermissionMapper.selectPermsByUserId(user.getId());
        List<String> permCodes = perms.stream()
                .filter(p -> p.getPermCode() != null)
                .map(SysPermission::getPermCode).toList();

        String accessToken = jwtUtil.generateAccessToken(user.getId(), "admin");
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        return AdminLoginResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .roles(roleCodes)
                .permissions(permCodes)
                .build();
    }

    @Override
    public TokenResp refreshToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID.getCode(), "Token无效或已过期");
        }
        Long userId = jwtUtil.getUserId(refreshToken);
        String newAccess = jwtUtil.generateAccessToken(userId, "admin");
        String newRefresh = jwtUtil.generateRefreshToken(userId);
        return TokenResp.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .userId(userId)
                .userType("admin")
                .build();
    }
}
