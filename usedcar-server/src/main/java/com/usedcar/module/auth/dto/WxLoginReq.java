package com.usedcar.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "微信登录请求")
public class WxLoginReq {

    @NotBlank(message = "loginCode不能为空")
    @Schema(description = "微信登录code")
    private String loginCode;

    @Schema(description = "手机号授权code（可选，首次登录时传入）")
    private String phoneCode;
}
