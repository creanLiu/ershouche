package com.usedcar.module.auth.controller;

import com.usedcar.common.result.R;
import com.usedcar.module.auth.dto.TokenResp;
import com.usedcar.module.auth.dto.WxLoginReq;
import com.usedcar.module.auth.service.MpAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "小程序认证", description = "小程序端登录认证接口")
@RestController
@RequestMapping("/api/v1/mp/auth")
@RequiredArgsConstructor
public class MpAuthController {

    private final MpAuthService mpAuthService;

    @Operation(summary = "微信登录", description = "使用微信loginCode和phoneCode登录")
    @PostMapping("/wx-login")
    public R<TokenResp> wxLogin(@Valid @RequestBody WxLoginReq req) {
        return R.ok(mpAuthService.wxLogin(req));
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public R<TokenResp> refreshToken(@RequestParam String refreshToken) {
        return R.ok(mpAuthService.refreshToken(refreshToken));
    }
}
