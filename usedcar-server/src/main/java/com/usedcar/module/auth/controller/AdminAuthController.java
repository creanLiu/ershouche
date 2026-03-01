package com.usedcar.module.auth.controller;

import com.usedcar.common.result.R;
import com.usedcar.module.auth.dto.AdminLoginReq;
import com.usedcar.module.auth.dto.AdminLoginResp;
import com.usedcar.module.auth.dto.TokenResp;
import com.usedcar.module.auth.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理后台认证", description = "管理后台登录认证接口")
@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public R<AdminLoginResp> login(@Valid @RequestBody AdminLoginReq req) {
        return R.ok(adminAuthService.login(req));
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public R<TokenResp> refreshToken(@RequestParam String refreshToken) {
        return R.ok(adminAuthService.refreshToken(refreshToken));
    }
}
