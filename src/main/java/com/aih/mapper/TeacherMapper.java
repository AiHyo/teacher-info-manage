package com.aih.mapper;

import com.aih.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 教师(用户) Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface TeacherMapper extends BaseMapper<Teacher> {
    public List<String> getRoleNameByTeacherId(Long tid);
}
