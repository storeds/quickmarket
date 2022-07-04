package com.quickmarket.portal.domain;

import com.quickmarket.mbg.model.PmsComment;
import com.quickmarket.mbg.model.PmsCommentReplay;
import lombok.Data;

import java.util.List;


@Data
public class PmsCommentParam extends PmsComment {
    private List<PmsCommentReplay> pmsCommentReplayList;
}
