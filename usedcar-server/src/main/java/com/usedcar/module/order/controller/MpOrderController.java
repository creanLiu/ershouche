package com.usedcar.module.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.exception.BusinessException;
import com.usedcar.common.result.R;
import com.usedcar.common.result.ResultCode;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.order.entity.OrderInfo;
import com.usedcar.module.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "订单-小程序", description = "小程序端订单接口")
@RestController
@RequestMapping("/api/v1/mp/orders")
@RequiredArgsConstructor
public class MpOrderController {

    private final OrderInfoService orderInfoService;

    @Operation(summary = "我的订单列表")
    @GetMapping
    public R<Page<OrderInfo>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getBuyerId, wxUserId)
                .orderByDesc(OrderInfo::getCreatedAt);
        if (status != null) wrapper.eq(OrderInfo::getStatus, status);
        return R.ok(orderInfoService.page(new Page<>(page, size), wrapper));
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public R<String> create(@RequestBody OrderInfo req) {
        Long wxUserId = SecurityUtils.getCurrentUserId();
        if (wxUserId == null) throw new BusinessException(ResultCode.UNAUTHORIZED);
        req.setBuyerId(wxUserId);
        req.setOrderNo("OD" + System.currentTimeMillis());
        req.setStatus(1);
        orderInfoService.save(req);
        return R.ok(req.getOrderNo());
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public R<OrderInfo> detail(@PathVariable Long id) {
        return R.ok(orderInfoService.getById(id));
    }
}
