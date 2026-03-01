package com.usedcar.module.auth.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.util.JwtUtil;
import com.usedcar.module.auth.dto.TokenResp;
import com.usedcar.module.auth.dto.WxLoginReq;
import com.usedcar.module.auth.service.MpAuthService;
import com.usedcar.module.user.entity.WxUser;
import com.usedcar.module.user.mapper.WxUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpAuthServiceImpl implements MpAuthService {

    private final WxMaService wxMaService;
    private final WxUserMapper wxUserMapper;
    private final JwtUtil jwtUtil;

    @Override
    public TokenResp wxLogin(WxLoginReq req) {
        // 1. code2session 获取 openid
        String openid;
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(req.getLoginCode());
            openid = session.getOpenid();
        } catch (Exception e) {
            log.error("微信登录失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.WX_LOGIN_FAIL.getCode(), "微信登录失败: " + e.getMessage());
        }

        // 2. 查找或创建用户
        WxUser wxUser = wxUserMapper.selectOne(
                new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenid, openid));
        if (wxUser == null) {
            wxUser = new WxUser();
            wxUser.setOpenid(openid);
            wxUser.setStatus(1);
        }

        // 3. 如果传了 phoneCode，获取手机号
        if (StringUtils.hasText(req.getPhoneCode())) {
            try {
                WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNoInfo(req.getPhoneCode());
                wxUser.setPhone(phoneInfo.getPhoneNumber());
            } catch (Exception e) {
                log.warn("获取手机号失败: {}", e.getMessage());
            }
        }

        wxUser.setLastLoginAt(LocalDateTime.now());
        if (wxUser.getId() == null) {
            wxUserMapper.insert(wxUser);
        } else {
            wxUserMapper.updateById(wxUser);
        }

        String accessToken = jwtUtil.generateAccessToken(wxUser.getId(), "mp");
        String refreshToken = jwtUtil.generateRefreshToken(wxUser.getId());

        return TokenResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(wxUser.getId())
                .userType("mp")
                .build();
    }

    @Override
    public TokenResp refreshToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID.getCode(), "Token无效或已过期");
        }
        Long userId = jwtUtil.getUserId(refreshToken);
        String newAccess = jwtUtil.generateAccessToken(userId, "mp");
        String newRefresh = jwtUtil.generateRefreshToken(userId);
        return TokenResp.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .userId(userId)
                .userType("mp")
                .build();
    }
}
