package com.quickmarket.seckill.domain;

import com.quickmarket.mbg.model.UmsMember;
import com.quickmarket.mbg.model.UmsMemberLevel;
import lombok.Data;


@Data
public class PortalMemberInfo extends UmsMember {
    private UmsMemberLevel umsMemberLevel;
}
