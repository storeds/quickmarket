package com.quickmarket.admin.service.impl;


import com.quickmarket.mbg.mapper.CmsPrefrenceAreaMapper;
import com.quickmarket.mbg.model.CmsPrefrenceArea;
import com.quickmarket.mbg.model.CmsPrefrenceAreaExample;
import com.quickmarket.admin.service.CmsPrefrenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选Service实现类
 * Created on
 */
@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
