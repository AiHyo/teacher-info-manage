package com.aih.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aih.entity.College;
import com.aih.mapper.CollegeMapper;
import com.aih.service.ICollegeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学院 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements ICollegeService {

    @Override
    public Page<College> getAllCollege(Integer pageNum, Integer pageSize, String collegeName) {
        Page<College> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<College> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(collegeName),College::getCollegeName,collegeName);
        queryWrapper.ne(College::getId,0L);
        return page(page,queryWrapper);
    }

    @Override
    public Long getCidByName(String collegeName) {
        return this.baseMapper.getCidByName(collegeName);
    }

    @Override
    public String getCollegeNameByCid(Long cid) {
        return this.baseMapper.getCollegeNameByCid(cid);
    }

}
