package com.usedcar.module.auth.service;

import com.usedcar.module.auth.dto.AdminLoginReq;
import com.usedcar.module.auth.dto.AdminLoginResp;
import com.usedcar.module.auth.dto.TokenResp;

public interface AdminAuthService {
    AdminLoginResp login(AdminLoginReq req);
    TokenResp refreshToken(String refreshToken);
}
