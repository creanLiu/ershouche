package com.usedcar.module.vehicle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.module.vehicle.entity.Brand;
import com.usedcar.module.vehicle.mapper.BrandMapper;
import com.usedcar.module.vehicle.service.BrandService;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
}
