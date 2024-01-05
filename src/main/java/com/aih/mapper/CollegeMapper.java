package com.aih.mapper;

import com.aih.entity.College;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * <p>
 * 学院 Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    @Select("select college_name from college where id = #{id} and deleted = 0")
    String getCollegeNameByCid(Long id);

    @Select("select id from college where college_name = #{collegeName} and deleted = 0")
    Long getCidByName(String collegeName);
}
