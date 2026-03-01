package com.usedcar.module.statistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usedcar.common.result.R;
import com.usedcar.module.customer.mapper.CustomerMapper;
import com.usedcar.module.order.entity.OrderInfo;
import com.usedcar.module.order.mapper.OrderInfoMapper;
import com.usedcar.module.vehicle.entity.Vehicle;
import com.usedcar.module.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "数据统计-后台", description = "管理后台数据统计接口")
@RestController
@RequestMapping("/api/v1/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final VehicleMapper vehicleMapper;
    private final OrderInfoMapper orderInfoMapper;
    private final CustomerMapper customerMapper;

    @Operation(summary = "Dashboard概览数据")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard() {
        Map<String, Object> data = new LinkedHashMap<>();

        long totalVehicles = vehicleMapper.selectCount(
                new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, 2));
        long totalOrders = orderInfoMapper.selectCount(null);
        long totalCustomers = customerMapper.selectCount(null);

        // 今日成交
        LocalDate today = LocalDate.now();
        long todayOrders = orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>()
                        .ge(OrderInfo::getCreatedAt, today.atStartOfDay())
                        .lt(OrderInfo::getCreatedAt, today.plusDays(1).atStartOfDay()));

        data.put("totalVehicles", totalVehicles);
        data.put("totalOrders", totalOrders);
        data.put("totalCustomers", totalCustomers);
        data.put("todayOrders", todayOrders);
        return R.ok(data);
    }

    @Operation(summary = "销售统计（近30天每日成交量）")
    @GetMapping("/sales")
    public R<Map<String, Object>> salesReport() {
        Map<String, Object> data = new LinkedHashMap<>();
        // 近30天成交订单
        LocalDate start = LocalDate.now().minusDays(29);
        List<OrderInfo> orders = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .ge(OrderInfo::getCreatedAt, start.atStartOfDay())
                        .eq(OrderInfo::getStatus, 5) // 已完成
                        .orderByAsc(OrderInfo::getCreatedAt));

        // 按日期聚合
        Map<String, Long> dailyCount = new LinkedHashMap<>();
        Map<String, BigDecimal> dailyAmount = new LinkedHashMap<>();
        for (int i = 0; i < 30; i++) {
            String date = start.plusDays(i).toString();
            dailyCount.put(date, 0L);
            dailyAmount.put(date, BigDecimal.ZERO);
        }
        for (OrderInfo o : orders) {
            String date = o.getCreatedAt().toLocalDate().toString();
            dailyCount.merge(date, 1L, Long::sum);
            if (o.getDealPrice() != null) {
                dailyAmount.merge(date, o.getDealPrice(), BigDecimal::add);
            }
        }
        data.put("dates", dailyCount.keySet());
        data.put("counts", dailyCount.values());
        data.put("amounts", dailyAmount.values());
        return R.ok(data);
    }

    @Operation(summary = "用户行为分析")
    @GetMapping("/user-behavior")
    public R<Map<String, Object>> userBehavior() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalCustomers", customerMapper.selectCount(null));
        data.put("message", "详细行为分析需接入埋点数据");
        return R.ok(data);
    }
}
