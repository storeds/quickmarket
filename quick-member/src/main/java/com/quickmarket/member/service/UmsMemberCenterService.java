package com.quickmarket.member.service;


import com.quickmarket.member.domain.PortalMemberInfo;


public interface UmsMemberCenterService {

    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    PortalMemberInfo getMemberInfo(Long memberId);
}
