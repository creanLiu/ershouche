package com.usedcar.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Token响应")
public class TokenResp {

    @Schema(description = "访问Token")
    private String accessToken;

    @Schema(description = "刷新Token")
    private String refreshToken;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户类型: mp=小程序用户, admin=管理员")
    private String userType;
}
