package com.usedcar.module.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usedcar.module.order.entity.OrderInfo;

public interface OrderInfoService extends IService<OrderInfo> {
    Page<OrderInfo> listOrders(Integer status, String keyword, int page, int size);
    void updateStatus(Long orderId, Integer newStatus, Long operatorId, String remark);
}
