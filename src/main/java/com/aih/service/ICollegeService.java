package com.aih.service;

import com.aih.entity.College;
import com.aih.entity.vo.OfficeDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学院 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface ICollegeService extends IService<College> {

    Page<College> getAllCollege(Integer pageNum, Integer pageSize, String collegeName);

    Long getCidByName(String collegeName);
}
