package com.usedcar.module.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.order.entity.OrderInfo;
import com.usedcar.module.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "订单管理-后台", description = "管理后台订单管理接口")
@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderInfoService orderInfoService;

    @Operation(summary = "订单列表")
    @GetMapping
    public R<Page<OrderInfo>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(orderInfoService.listOrders(status, keyword, page, size));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public R<OrderInfo> detail(@PathVariable Long id) {
        return R.ok(orderInfoService.getById(id));
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer newStatus = (Integer) body.get("status");
        String remark = (String) body.get("remark");
        Long operatorId = SecurityUtils.getCurrentUserId();
        orderInfoService.updateStatus(id, newStatus, operatorId, remark);
        return R.ok();
    }
}
