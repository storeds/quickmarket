package com.quickmarket.product.vo;

import com.quickmarket.mbg.model.PmsProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:31
 * @description:
 **/
public class IndexVo {
    /**
     * 全部商品
     */
    private Integer productall;

    /**
     * 订单数本月
     */
    private Integer mothOd;

    /**
     * 订单数本周
     */
    private Integer weekOd;

    /**
     * 销售额本月
     */
    private Integer mothSale;

    /**
     * 销售额本周
     */
    private Integer weekSale;

    /**
     * 展示有各个订单的数量
     */
    private List<OrderCount> orderCount;

    /**
     * 展示今日订单总数， 今日销售总数
     */
    private TodayAllCount todayAllCount;

    /**
     * 已经下架和上架的商品
     */
    private List<UpAndDown> upAndDown;

    public IndexVo() {
    }

    public IndexVo(Integer productall, Integer mothOd, Integer weekOd, Integer mothSale, Integer weekSale, List<OrderCount> orderCount, TodayAllCount todayAllCount, List<UpAndDown> upAndDown) {
        this.productall = productall;
        this.mothOd = mothOd;
        this.weekOd = weekOd;
        this.mothSale = mothSale;
        this.weekSale = weekSale;
        this.orderCount = orderCount;
        this.todayAllCount = todayAllCount;
        this.upAndDown = upAndDown;
    }

    public Integer getProductall() {
        return productall;
    }

    public void setProductall(Integer productall) {
        this.productall = productall;
    }

    public Integer getMothOd() {
        return mothOd;
    }

    public void setMothOd(Integer mothOd) {
        this.mothOd = mothOd;
    }

    public Integer getWeekOd() {
        return weekOd;
    }

    public void setWeekOd(Integer weekOd) {
        this.weekOd = weekOd;
    }

    public Integer getMothSale() {
        return mothSale;
    }

    public void setMothSale(Integer mothSale) {
        this.mothSale = mothSale;
    }

    public Integer getWeekSale() {
        return weekSale;
    }

    public void setWeekSale(Integer weekSale) {
        this.weekSale = weekSale;
    }

    public List<OrderCount> getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(List<OrderCount> orderCount) {
        this.orderCount = orderCount;
    }

    public TodayAllCount getTodayAllCount() {
        return todayAllCount;
    }

    public void setTodayAllCount(TodayAllCount todayAllCount) {
        this.todayAllCount = todayAllCount;
    }

    public List<UpAndDown> getUpAndDown() {
        return upAndDown;
    }

    public void setUpAndDown(List<UpAndDown> upAndDown) {
        this.upAndDown = upAndDown;
    }

    @Override
    public String toString() {
        return "IndexVo{" +
                "productall=" + productall +
                ", mothOd=" + mothOd +
                ", weekOd=" + weekOd +
                ", mothSale=" + mothSale +
                ", weekSale=" + weekSale +
                ", orderCount=" + orderCount +
                ", todayAllCount=" + todayAllCount +
                ", upAndDown=" + upAndDown +
                '}';
    }
}
