package com.usedcar.module.auth.service;

import com.usedcar.module.auth.dto.TokenResp;
import com.usedcar.module.auth.dto.WxLoginReq;

public interface MpAuthService {
    TokenResp wxLogin(WxLoginReq req);
    TokenResp refreshToken(String refreshToken);
}
