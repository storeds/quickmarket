package com.quickmarket.communicat.reposity;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 10:31
 * @description:
 **/
public interface UserGroupRepository {

    List<Integer> findGroupIdByUserId(Integer userId);

}
