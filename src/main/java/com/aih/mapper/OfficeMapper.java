package com.aih.mapper;

import com.aih.entity.Office;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 教研室 Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Mapper
public interface OfficeMapper extends BaseMapper<Office> {
    @Select("select office_name from office where id = #{id}")
    String getOfficeNameById(Long id);
}
