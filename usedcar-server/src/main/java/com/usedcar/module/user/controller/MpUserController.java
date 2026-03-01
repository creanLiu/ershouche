package com.usedcar.module.user.controller;

import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.R;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.customer.entity.CustomerBehavior;
import com.usedcar.module.customer.mapper.CustomerBehaviorMapper;
import com.usedcar.module.user.entity.WxUser;
import com.usedcar.module.user.mapper.WxUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Tag(name = "用户信息-小程序", description = "小程序端用户信息接口")
@RestController
@RequestMapping("/api/v1/mp/user")
@RequiredArgsConstructor
public class MpUserController {

    private final WxUserMapper wxUserMapper;
    private final CustomerBehaviorMapper customerBehaviorMapper;

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public R<WxUser> profile() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        WxUser user = wxUserMapper.selectById(userId);
        if (user != null) user.setOpenid(null); // 不返回openid
        return R.ok(user);
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/profile")
    public R<Void> updateProfile(@RequestBody WxUser req) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        req.setId(userId);
        req.setOpenid(null); // 不允许修改openid
        wxUserMapper.updateById(req);
        return R.ok();
    }

    @Operation(summary = "上报行为数据")
    @PostMapping("/behavior")
    public R<Void> reportBehavior(@RequestBody Map<String, Object> body) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) return R.ok();
        CustomerBehavior behavior = new CustomerBehavior();
        behavior.setWxUserId(userId);
        behavior.setEventType((String) body.get("eventType"));
        behavior.setTargetType((String) body.get("targetType"));
        behavior.setTargetId(body.get("targetId") != null ? body.get("targetId").toString() : null);
        behavior.setCreatedAt(LocalDateTime.now());
        customerBehaviorMapper.insert(behavior);
        return R.ok();
    }
}
