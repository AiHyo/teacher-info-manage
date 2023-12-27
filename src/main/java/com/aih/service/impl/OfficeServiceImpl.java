package com.aih.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aih.entity.College;
import com.aih.entity.Office;
import com.aih.entity.vo.OfficeDto;
import com.aih.mapper.CollegeMapper;
import com.aih.mapper.OfficeMapper;
import com.aih.service.IOfficeService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 教研室 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class OfficeServiceImpl extends ServiceImpl<OfficeMapper, Office> implements IOfficeService {
    @Autowired
    private CollegeMapper collegeMapper;
    @Override
    public Page<OfficeDto> getOfficeByCollege(Integer pageNum, Integer pageSize, String officeName) {
        Long cid = UserInfoContext.getUser().getCid();
        Page<Office> pageInfo = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Office> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Office::getCid,cid);
        queryWrapper.orderByAsc(Office::getCid); //根据学院id排序
        queryWrapper.like(StrUtil.isNotBlank(officeName),Office::getOfficeName,officeName); //officeName不为空时,模糊查询
        this.baseMapper.selectPage(pageInfo, queryWrapper);
        //Dto处理
        List<OfficeDto> collect = pageInfo.getRecords().stream().map((item) -> {
            OfficeDto dto = new OfficeDto();
            BeanUtils.copyProperties(item, dto);
            College college = collegeMapper.selectById(item.getCid());
            if (college != null) {
                dto.setCollegeName(college.getCollegeName());
            }
            return dto;
        }).collect(Collectors.toList());
        Page<OfficeDto> dtoPage = new Page<>(pageNum,pageSize);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public Page<OfficeDto> getAllOffice(Integer pageNum, Integer pageSize, String officeName) {
        Page<Office> pageInfo = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Office> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Office::getCid); //根据学院id排序
        queryWrapper.like(StrUtil.isNotBlank(officeName),Office::getOfficeName,officeName); //officeName不为空时,模糊查询
        this.baseMapper.selectPage(pageInfo, queryWrapper);
        //Dto处理
        List<OfficeDto> collect = pageInfo.getRecords().stream().map((item) -> {
            OfficeDto dto = new OfficeDto();
            BeanUtils.copyProperties(item, dto);
            College college = collegeMapper.selectById(item.getCid());
            if (college != null) {
                dto.setCollegeName(college.getCollegeName());
            }
            return dto;
        }).collect(Collectors.toList());
        Page<OfficeDto> dtoPage = new Page<>(pageNum,pageSize);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }
}
