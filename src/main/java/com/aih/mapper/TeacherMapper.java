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

    @Select("select zw_id from teacher where zw_oid = #{oid} and zw_deleted = 0")
    List<Long> getTeacherIdsByOid(Long oid);

    @Select("select zw_id from teacher where zw_oid = #{oid} and zw_is_auditor = 1 and zw_deleted = 0")
    List<Long> getAuditorPowerIdsByOid(Long oid);

    @Select("select zw_id from teacher where zw_oid = #{oid} and zw_is_auditor = 0 and zw_deleted = 0")
    List<Long> getCanAuditTidsByOid(Long oid);

    @Select("select zw_id from teacher where zw_cid = #{cid} and zw_is_auditor = 1 and zw_deleted = 0")
    List<Long> getCanAuditTidsByCid(Long cid);

    @Select("select zw_teacher_name from teacher where zw_id = #{tid} and zw_deleted = 0")
    String getTeacherNameByTid(Long tid);

    @Select("select zw_id from teacher where zw_phone = #{phone} and zw_deleted = 0")
    List<Long> getTidsByPhone(String phone);

    @Select("select zw_id from teacher where zw_id_number = #{idNumber} and zw_deleted = 0")
    List<Long> getTidsByIdNumber(String idNumber);

    @Select("select zw_id from teacher where zw_desk_id = #{deskId} and zw_deleted = 0")
    List<Long> getTidsByDeskId(Long deskId);
}
