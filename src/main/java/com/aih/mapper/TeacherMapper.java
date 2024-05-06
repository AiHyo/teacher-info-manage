package com.aih.mapper;

import com.aih.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * 教师(用户) Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    List<String> getRoleNameByTeacherId(Long tid);

    @Select("select zw_id from teacher where zw_oid = #{oid}")
    List<Long> getTeacherIdsByOid(Long oid);

    @Select("select zw_id from teacher where zw_oid = #{oid} and zw_is_auditor = 1")
    List<Long> getAuditorPowerIdsByOid(Long oid);

    @Select("select zw_id from teacher where zw_oid = #{oid} and zw_is_auditor = 0")
    List<Long> getCanAuditTidsByOid(Long oid);

    @Select("select zw_id from teacher where zw_cid = #{cid} and zw_is_auditor = 1")
    List<Long> getCanAuditTidsByCid(Long cid);

    @Select("select zw_teacher_name from teacher where zw_id = #{tid}")
    String getTeacherNameByTid(Long tid);
}
