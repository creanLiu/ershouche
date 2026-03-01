package com.usedcar.module.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.ResultCode;
import com.usedcar.module.order.entity.OrderInfo;
import com.usedcar.module.order.entity.OrderStatusLog;
import com.usedcar.module.order.mapper.OrderInfoMapper;
import com.usedcar.module.order.mapper.OrderStatusLogMapper;
import com.usedcar.module.order.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    private final OrderStatusLogMapper orderStatusLogMapper;

    @Override
    public Page<OrderInfo> listOrders(Integer status, String keyword, int page, int size) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(OrderInfo::getStatus, status);
        if (StringUtils.hasText(keyword)) wrapper.like(OrderInfo::getOrderNo, keyword);
        wrapper.orderByDesc(OrderInfo::getCreatedAt);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void updateStatus(Long orderId, Integer newStatus, Long operatorId, String remark) {
        OrderInfo order = this.getById(orderId);
        if (order == null) throw new BusinessException(ResultCode.ORDER_NOT_FOUND);

        Integer fromStatus = order.getStatus();
        order.setStatus(newStatus);
        this.updateById(order);

        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(newStatus);
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        log.setCreatedAt(LocalDateTime.now());
        orderStatusLogMapper.insert(log);
    }
}
