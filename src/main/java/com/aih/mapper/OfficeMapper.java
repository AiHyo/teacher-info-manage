package com.aih.mapper;

import com.aih.entity.Office;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Select("select office_name from office where id = #{id} and deleted = 0")
    String getOfficeNameByOid(Long id);

    @Select("<script>" +
                "select * from office where cid = #{cid} and deleted = 0 " +
                "<if test='officeName!=null'> and office_name LIKE CONCAT('%', #{officeName}, '%')</if>" +
            "</script>")
    List<Office> getOfficeList(Long cid, String officeName);
}
