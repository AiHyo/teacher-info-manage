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
    @Select("select zw_office_name from office where zw_id = #{id} and zw_deleted = 0")
    String getOfficeNameByOid(Long id);

    @Select("<script>" +
                "select * from office where zw_cid = #{cid} and zw_deleted = 0 " +
                "<if test='officeName!=null'> and zw_office_name LIKE CONCAT('%', #{officeName}, '%')</if>" +
            "</script>")
    List<Office> getOfficeList(Long cid, String officeName);

    @Select("select zw_id from office where zw_office_name = #{officeName} and zw_deleted = 0")
    Long getOidByNameAndCid(String officeName);

    @Select("select * from office where zw_cid = #{cid} and zw_deleted = 0")
    List<Office> getOfficeListByCid(Long cid);

    @Select("select zw_cid from office where zw_id = #{oid} and zw_deleted = 0")
    Long getCidById(Long oid);

    @Select("select zw_id from office where zw_office_name = #{officeName} and zw_cid = #{cid} and zw_deleted = 0")
    Long getOidByNameAndCid(String officeName, Long cid);
}
