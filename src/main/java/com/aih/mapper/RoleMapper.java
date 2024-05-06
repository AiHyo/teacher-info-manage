package com.aih.mapper;

import com.aih.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from role where zw_id in (#{rids})")
    List<Role> selectByRids(List<Long> rids);
}
