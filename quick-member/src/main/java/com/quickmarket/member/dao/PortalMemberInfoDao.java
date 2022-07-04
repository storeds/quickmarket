package com.quickmarket.member.dao;


import com.quickmarket.member.domain.PortalMemberInfo;


public interface PortalMemberInfoDao {
    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    PortalMemberInfo getMemberInfo(Long memberId);
}
