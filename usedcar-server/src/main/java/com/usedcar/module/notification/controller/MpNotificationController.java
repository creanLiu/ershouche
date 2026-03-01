package com.usedcar.module.notification.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.R;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.notification.entity.Notification;
import com.usedcar.module.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "消息通知-小程序", description = "小程序端消息通知接口")
@RestController
@RequestMapping("/api/v1/mp/notifications")
@RequiredArgsConstructor
public class MpNotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "消息列表")
    @GetMapping
    public R<Page<Notification>> list(
            @RequestParam(required = false) Integer notifyType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getWxUserId, wxUserId)
                .orderByDesc(Notification::getCreatedAt);
        if (notifyType != null) wrapper.eq(Notification::getNotifyType, notifyType);
        return R.ok(notificationService.page(new Page<>(page, size), wrapper));
    }

    @Operation(summary = "标记已读")
    @PutMapping("/{id}/read")
    public R<Void> markRead(@PathVariable Long id) {
        Notification n = notificationService.getById(id);
        if (n != null) {
            n.setIsRead(1);
            n.setReadAt(LocalDateTime.now());
            notificationService.updateById(n);
        }
        return R.ok();
    }

    @Operation(summary = "全部已读")
    @PutMapping("/read-all")
    public R<Void> readAll() {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        Notification update = new Notification();
        update.setIsRead(1);
        update.setReadAt(LocalDateTime.now());
        notificationService.update(update,
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getWxUserId, wxUserId)
                        .eq(Notification::getIsRead, 0));
        return R.ok();
    }
}
