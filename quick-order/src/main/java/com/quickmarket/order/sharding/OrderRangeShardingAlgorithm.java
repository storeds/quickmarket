package com.quickmarket.order.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;


@Slf4j
public class OrderRangeShardingAlgorithm implements RangeShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
        // 真实节点
        collection.stream().forEach((item)->{
            log.info("actual node table:{}",item);
        });

        log.info("logic table name:{},rout column:{}",rangeShardingValue.getLogicTableName(),rangeShardingValue.getColumnName());

        //区间分片
        log.info("range:{}",rangeShardingValue.getValueRange());

        return null; //返回你要路由的表的集合
    }
}
