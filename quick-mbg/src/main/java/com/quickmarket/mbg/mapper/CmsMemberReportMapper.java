package com.quickmarket.mbg.mapper;

import com.quickmarket.mbg.model.CmsMemberReport;
import com.quickmarket.mbg.model.CmsMemberReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsMemberReportMapper {
    long countByExample(CmsMemberReportExample example);

    int deleteByExample(CmsMemberReportExample example);

    int insert(CmsMemberReport record);

    int insertSelective(CmsMemberReport record);

    List<CmsMemberReport> selectByExample(CmsMemberReportExample example);

    int updateByExampleSelective(@Param("record") CmsMemberReport record, @Param("example") CmsMemberReportExample example);

    int updateByExample(@Param("record") CmsMemberReport record, @Param("example") CmsMemberReportExample example);
}
