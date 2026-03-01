package com.usedcar.module.customer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usedcar.common.result.R;
import com.usedcar.common.security.SecurityUtils;
import com.usedcar.module.customer.entity.Customer;
import com.usedcar.module.customer.entity.CustomerFollowup;
import com.usedcar.module.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "客户管理-后台", description = "管理后台客户管理接口")
@RestController
@RequestMapping("/api/v1/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @Operation(summary = "客户列表")
    @GetMapping
    public R<Page<Customer>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer intentLevel,
            @RequestParam(required = false) Integer stage,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(customerService.listCustomers(keyword, intentLevel, stage, page, size));
    }

    @Operation(summary = "客户详情")
    @GetMapping("/{id}")
    public R<Customer> detail(@PathVariable Long id) {
        return R.ok(customerService.getById(id));
    }

    @Operation(summary = "更新客户信息")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerService.updateById(customer);
        return R.ok();
    }

    @Operation(summary = "跟进记录列表")
    @GetMapping("/{id}/followups")
    public R<List<CustomerFollowup>> followups(@PathVariable Long id) {
        return R.ok(customerService.getFollowups(id));
    }

    @Operation(summary = "新增跟进记录")
    @PostMapping("/{id}/followups")
    public R<Void> addFollowup(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long operatorId = SecurityUtils.getCurrentUserId();
        Integer followupType = body.get("followupType") != null ? (Integer) body.get("followupType") : null;
        customerService.addFollowup(id, operatorId,
                (String) body.get("content"), followupType,
                (String) body.get("result"), (String) body.get("nextFollowupAt"));
        return R.ok();
    }
}
