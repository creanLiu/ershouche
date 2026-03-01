package com.usedcar.module.customer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usedcar.module.customer.entity.Customer;
import com.usedcar.module.customer.entity.CustomerFollowup;

import java.util.List;

public interface CustomerService extends IService<Customer> {
    Page<Customer> listCustomers(String keyword, Integer intentLevel, Integer stage, int page, int size);
    List<CustomerFollowup> getFollowups(Long customerId);
    void addFollowup(Long customerId, Long operatorId, String content, Integer followupType, String result, String nextFollowupAt);
}
