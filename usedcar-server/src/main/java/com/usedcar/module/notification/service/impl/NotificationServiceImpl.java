package com.usedcar.module.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.module.notification.entity.Notification;
import com.usedcar.module.notification.mapper.NotificationMapper;
import com.usedcar.module.notification.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
}
