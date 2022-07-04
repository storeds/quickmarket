package com.quickmarket.order.dao;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;


public interface MiaoShaStockDao {

    //减库存
    Integer descStock(@Param("id") Long id, @Param("stock") Integer stock);


    /**
     * 乐观锁减库存
     */
    Integer descNormalStock(@Param("id") Long id, @Param("oldstock") Integer oldstock,
                            @Param("skuId") Long skuId, @Param("quity") Integer quity);

    /**
     * 产品乐观锁减库存
     * @param id
     *      秒杀活动库存记录ID
     * @return
     */
    Integer descStockInVersion(@Param("id") Long id, @Param("oldStock") Integer oldStock, @Param("newStock") Integer newStock);

    /**
     * 查询当前的缓存库存
     * @param id
     * @return
     */
    Integer selectMiaoShaStock(@Param("id") Long id);


    /*-------------------------悲观锁实现--------------------------*/
    /**
     * 查询加锁
     * @param id
     * @return
     */
    Integer selectMiaoShaStockInLock(@Param("id") Long id);
    /**
     *
     * @param id
     * @param stock
     * @return
     */
    Integer descStockInLock(@Param("id") Long id, @Param("stock") Integer stock);
}
