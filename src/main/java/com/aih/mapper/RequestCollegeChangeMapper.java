package com.aih.mapper;

import com.aih.entity.RequestCollegeChange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-12-19
 */
public interface RequestCollegeChangeMapper extends BaseMapper<RequestCollegeChange> {
    @Select("select count(*) from request_college_change where zw_tid = #{tid} and zw_audit_status = 0 and zw_deleted = 0")
    Integer getUnAuditCountByTid(Long tid);
}
