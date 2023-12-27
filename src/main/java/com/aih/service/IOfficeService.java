package com.aih.service;

import com.aih.entity.Office;
import com.aih.entity.vo.OfficeDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 教研室 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */

public interface IOfficeService extends IService<Office> {
    Page<OfficeDto> getOfficeByCollege(Integer pageNum, Integer pageSize, String officeName);

    Page<OfficeDto> getAllOffice(Integer pageNum, Integer pageSize, String officeName);
}
