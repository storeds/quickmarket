package com.quickmarket.canal.mq;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.quickmarket.canal.domain.PmsProductParam;
import com.quickmarket.canal.util.ClassUtil;
import com.quickmarket.canal.util.RedisOpsUtil;
import com.quickmarket.common.constant.RedisKeyPrefixConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "productChange",consumeMode= ConsumeMode.CONCURRENTLY)
public class RefreshCacheListener implements RocketMQListener<FlatMessage> {

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    private final static String PRODUCT = "pms_product";
    private final static String SKU = "pms_sku_stock";

    /**
     * 异步下单消费消息
     * @param flatMessage
     */
    @Override
    public void onMessage(FlatMessage flatMessage) {
        if (!flatMessage.getDatabase().equals("micromall")) return;
        log.info("{}",flatMessage);
        log.info("database:{},event-type:{},old-row-data:{},new-row-data:{}",
                flatMessage.getDatabase(),
                flatMessage.getType(),
                flatMessage.getOld(),
                flatMessage.getData());

        //修改后的新记录
        List<Map<String, String>> records = flatMessage.getData();
        //修改前的数据
        List<Map<String, String>> old = flatMessage.getOld();

        switch (flatMessage.getType().toUpperCase()){
            case "UPDATE":
                updateCache(records,old,flatMessage.getTable());
                break;
            case "DELETE":
                records.stream().forEach((item)->{
                    //删除缓存
                    redisOpsUtil.delete(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + item.get("id"));
                });
                break;
        }
    }

    public void updateCache(List<Map<String, String>> records,List<Map<String, String>> old,String table){
        int index = 0;
        /*
         * 被更改的Row所有更改的行
         */
        for(Map<String,String> row : old){
            Map<String, String> currentRow = records.get(index);
            String redisKey = RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + getProductId(currentRow,table);
            PmsProductParam product = redisOpsUtil.get(redisKey,PmsProductParam.class);
            if(!ObjectUtils.isEmpty(product)){
                Iterator<Map.Entry<String, String>> iterator = row.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry entry = iterator.next();
                    String key = (String) entry.getKey();
                    //刷新产品数据
                    product = refresh(product,table,key,currentRow);
                }
                /*
                 * 更新缓存内容,并设置过期时间
                 */
                long expired = redisOpsUtil.getExpired(redisKey, TimeUnit.SECONDS);
                redisOpsUtil.set(redisKey,product,expired,TimeUnit.SECONDS);
            }
            ++index;
        }
    }

    /**
     * 更新缓存数据
     * @param product
     * @param table
     * @param key
     * @param currentRow
     * @return
     */
    private PmsProductParam refresh(PmsProductParam product,String table,String key,Map<String, String> currentRow){
        if(PRODUCT.equals(table)){
            ClassUtil.callSetterMethod(product,ClassUtil.getSetterMethodName(key),currentRow.get(key));
        }else if(SKU.equals(table)){
            product.getSkuStockList().stream().forEach((item)->{
                if(item.getId() == Long.parseLong(currentRow.get("id"))){
                    ClassUtil.callSetterMethod(item,ClassUtil.getSetterMethodName(key),currentRow.get(key));
                }
            });
        }
        return product;
    }

    /*
     * 获取产品ID
     */
    private String getProductId(Map<String, String> row,String table){
        if(PRODUCT.equals(table)){
            return row.get("id");
        }else{
            return row.get("product_id");
        }
    }

}
