package com.quickmarket.quick.service;


import com.quickmarket.common.api.CommonResult;
import com.quickmarket.feign.UmsMemberFeignService;
import com.quickmarket.mbg.model.UmsMember;
import com.quickmarket.quick.domain.MemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



/**
* @desc: 类的描述:认证服务器加载用户的类
* @createDate: 2022/1/21 21:29
* @version: 1.0
*/
@Slf4j
@Component
public class QuickUserDetailService implements UserDetailsService {
    @Autowired
    UmsMemberFeignService umsMemberFeignService;

    /**
     * 加载用户信息
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {


        if(StringUtils.isEmpty(userName)) {
            log.warn("用户登陆用户名为空:{}",userName);
            throw new UsernameNotFoundException("用户名不能为空");
        }

        UmsMember umsMember = getbyUsername(userName);

        if(null == umsMember) {
            log.warn("根据用户名没有查询到对应的用户信息:{}",userName);
        }
        log.info("根据用户名:{}获取用户登陆信息:{}",userName,umsMember);
        MemberDetails memberDetails = new MemberDetails(umsMember);

        return memberDetails;
    }


    /**
     * 获取用户信息
     * @param username
     * @return
     */
    public UmsMember getbyUsername(String username) {
        // 同过fegin获取会员信息
        CommonResult<UmsMember> umsMember = umsMemberFeignService.loadUserByUsername(username);
        return umsMember.getData();
    }



}
