package com.quickmarket.member.service;


import com.quickmarket.member.domain.MemberProductCollection;

import java.util.List;

/**
 * 会员收藏Service
 */
public interface MemberCollectionService {

    int addProduct(MemberProductCollection productCollection);

    int deleteProduct(Long memberId, Long productId);

    List<MemberProductCollection> listProduct(Long memberId);
}
