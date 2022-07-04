package com.quickmarket.admin.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-04-01 10:33
 * @description:
 **/
public interface UpdateLoginDao {
    void updateLogin(@Param("username") String username);
}
