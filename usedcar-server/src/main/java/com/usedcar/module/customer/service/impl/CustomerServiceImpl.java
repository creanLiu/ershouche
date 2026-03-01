package com.usedcar.module.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.module.customer.entity.Customer;
import com.usedcar.module.customer.entity.CustomerFollowup;
import com.usedcar.module.customer.mapper.CustomerFollowupMapper;
import com.usedcar.module.customer.mapper.CustomerMapper;
import com.usedcar.module.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private final CustomerFollowupMapper customerFollowupMapper;

    @Override
    public Page<Customer> listCustomers(String keyword, Integer intentLevel, Integer stage, int page, int size) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Customer::getName, keyword).or().like(Customer::getPhone, keyword);
        }
        if (intentLevel != null) wrapper.eq(Customer::getIntentLevel, intentLevel);
        if (stage != null) wrapper.eq(Customer::getStage, stage);
        wrapper.orderByDesc(Customer::getCreatedAt);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public List<CustomerFollowup> getFollowups(Long customerId) {
        return customerFollowupMapper.selectList(
                new LambdaQueryWrapper<CustomerFollowup>()
                        .eq(CustomerFollowup::getCustomerId, customerId)
                        .orderByDesc(CustomerFollowup::getCreatedAt));
    }

    @Override
    public void addFollowup(Long customerId, Long operatorId, String content,
                            Integer followupType, String result, String nextFollowupAt) {
        CustomerFollowup followup = new CustomerFollowup();
        followup.setCustomerId(customerId);
        followup.setOperatorId(operatorId);
        followup.setContent(content);
        followup.setFollowupType(followupType);
        followup.setResult(result);
        followup.setCreatedAt(LocalDateTime.now());
        if (StringUtils.hasText(nextFollowupAt)) {
            followup.setNextFollowupAt(LocalDateTime.parse(nextFollowupAt));
        }
        customerFollowupMapper.insert(followup);

        if (StringUtils.hasText(nextFollowupAt)) {
            Customer customer = this.getById(customerId);
            if (customer != null) {
                customer.setNextFollowupAt(LocalDateTime.parse(nextFollowupAt));
                this.updateById(customer);
            }
        }
    }
}
