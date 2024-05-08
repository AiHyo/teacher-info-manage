package com.aih.mapper;

import com.aih.entity.TeacherRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
public interface TeacherRoleMapper extends BaseMapper<TeacherRole> {
    @Delete("delete from teacher_role where zw_tid = #{tid}") //直接删不做逻辑删除
    void deleteTeacherRoleByTid(Long tid);

    @Select("select * from teacher_role where zw_tid = #{tid}")
    List<TeacherRole> selectByTid(Long tid);
}
