package com.quickmarket.admin.service.impl;

import com.quickmarket.mbg.mapper.OmsCompanyAddressMapper;
import com.quickmarket.mbg.model.OmsCompanyAddress;
import com.quickmarket.mbg.model.OmsCompanyAddressExample;
import com.quickmarket.admin.service.OmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created on
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
