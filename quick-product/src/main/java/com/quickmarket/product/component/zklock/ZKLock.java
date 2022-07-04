package com.quickmarket.product.component.zklock;


public interface ZKLock {

    boolean lock(String lockpath);

    boolean unlock(String lockpath);

}
