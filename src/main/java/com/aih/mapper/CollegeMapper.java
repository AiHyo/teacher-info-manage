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
    @Select("select zw_college_name from college where zw_id = #{id} and zw_deleted = 0")
    String getCollegeNameByCid(Long id);

    @Select("select zw_id from college where zw_college_name = #{collegeName} and zw_deleted = 0")
    Long getCidByName(String collegeName);
}
