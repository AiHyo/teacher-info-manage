package com.aih.service;

import com.aih.entity.Office;
import com.aih.entity.vo.OfficeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 教研室 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */

public interface IOfficeService extends IService<Office> {
    Page<OfficeVo> getOfficeByCollege(Integer pageNum, Integer pageSize, String officeName);

    Page<OfficeVo> getAllOffice(Integer pageNum, Integer pageSize, String officeName, Long cid);

    String getOfficeNameByOid(Long id);

    List<Office> getOfficeListByCid(Long cid);

    Long getCidById(Long oid);

    Long getOidByNameAndCid(String officeName, Long cid);
}
