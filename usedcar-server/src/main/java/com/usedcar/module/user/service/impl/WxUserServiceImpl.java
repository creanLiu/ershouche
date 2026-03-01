package com.usedcar.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usedcar.module.user.entity.WxUser;
import com.usedcar.module.user.mapper.WxUserMapper;
import com.usedcar.module.user.service.WxUserService;
import org.springframework.stereotype.Service;

@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {
}
