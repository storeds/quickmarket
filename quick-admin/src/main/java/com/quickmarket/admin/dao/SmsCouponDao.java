package com.quickmarket.admin.dao;

import com.quickmarket.admin.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券管理自定义查询Dao
 * Created on
 */
public interface SmsCouponDao {
    SmsCouponParam getItem(@Param("id") Long id);
}
