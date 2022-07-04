package com.quickmarket.order.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;


@Slf4j
public class OrderPreciseShardingAlgorithm implements PreciseShardingAlgorithm {
    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {

        // 真实节点
        collection.stream().forEach((item)->{
            log.info("actual node table:{}",item);
        });

        log.info("logic table name:{},rout column:{}",preciseShardingValue.getLogicTableName(),preciseShardingValue.getColumnName());

        //精确分片
        log.info("column value:{}",preciseShardingValue.getValue());
        return null;
    }
}
