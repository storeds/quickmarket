package com.quickmarket.order.service.impl;

import com.quickmarket.order.dao.PortalOrderDao;
import com.quickmarket.order.domain.OmsOrderReturnApplyParam;
import com.quickmarket.mbg.mapper.OmsOrderReturnApplyMapper;
import com.quickmarket.mbg.model.OmsOrderReturnApply;
import com.quickmarket.order.service.OmsPortalOrderReturnApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单退货管理Service实现类
 */
@Service
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;
    @Autowired
    private PortalOrderDao portalOrderDao;

    @Override
    @Transactional
    public int create(OmsOrderReturnApplyParam returnApply) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply,realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        portalOrderDao.updateOrderStatu(returnApply.getOrderId(), 4);
        return returnApplyMapper.insert(realApply);
    }

    @Override
    public int receive(OmsOrderReturnApplyParam returnApply) {
        return portalOrderDao.updateOrderStatu(returnApply.getOrderId(), 3);
    }
}
