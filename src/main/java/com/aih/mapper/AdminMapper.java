package com.aih.mapper;

import com.aih.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 管理员 Mapper 接口
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select id from admin where cid = #{cid}")
    List<Long> getAdminIdsByCid(Long cid);
}
