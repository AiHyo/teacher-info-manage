package com.aih.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aih.entity.College;
import com.aih.entity.Office;
import com.aih.entity.vo.OfficeVo;
import com.aih.mapper.CollegeMapper;
import com.aih.mapper.OfficeMapper;
import com.aih.service.IOfficeService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Equivalence;
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
    public Page<OfficeVo> getOfficeByCollege(Integer pageNum, Integer pageSize, String officeName) {
        Long cid = UserInfoContext.getUser().getCid();
        Page<Office> pageInfo = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Office> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Office::getCid,cid);
        queryWrapper.orderByAsc(Office::getCid); //根据学院id排序
        queryWrapper.like(StrUtil.isNotBlank(officeName),Office::getOfficeName,officeName); //officeName不为空时,模糊查询
        this.baseMapper.selectPage(pageInfo, queryWrapper);
        //Dto处理
        List<OfficeVo> collect = pageInfo.getRecords().stream().map((item) -> {
            OfficeVo dto = new OfficeVo();
            BeanUtils.copyProperties(item, dto);
            College college = collegeMapper.selectById(item.getCid());
            if (college != null) {
                dto.setCollegeName(college.getCollegeName());
            }
            return dto;
        }).collect(Collectors.toList());
        Page<OfficeVo> dtoPage = new Page<>(pageNum,pageSize);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public Page<OfficeVo> getAllOffice(Integer pageNum, Integer pageSize, String officeName, Long cid) {
        Page<Office> pageInfo = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Office> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(cid!=null,Office::getCid,cid);
        queryWrapper.orderByAsc(Office::getCid); //根据学院id排序
        queryWrapper.like(StrUtil.isNotBlank(officeName),Office::getOfficeName,officeName); //officeName不为空时,模糊查询
        queryWrapper.ne(Office::getId, 0L); //排除id为0的数据
        this.baseMapper.selectPage(pageInfo, queryWrapper);
        //Dto处理
        List<OfficeVo> collect = pageInfo.getRecords().stream().map((item) -> {
            OfficeVo dto = new OfficeVo();
            BeanUtils.copyProperties(item, dto);
            College college = collegeMapper.selectById(item.getCid());
            if (college != null) {
                dto.setCollegeName(college.getCollegeName());
            }
            return dto;
        }).collect(Collectors.toList());
        Page<OfficeVo> dtoPage = new Page<>(pageNum,pageSize);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public Long getOidByName(String officeName) {
        return this.baseMapper.getOidByName(officeName);
    }

    @Override
    public String getOfficeNameByOid(Long id) {
        return this.baseMapper.getOfficeNameByOid(id);
    }

    @Override
    public List<Office> getOfficeListByCid(Long cid) {
        return this.baseMapper.getOfficeListByCid(cid);
    }

    @Override
    public Long getCidById(Long oid) {
        return this.baseMapper.getCidById(oid);
    }

    @Override
    public Long getOidByNameAndCid(String officeName, Long cid) {
        return this.baseMapper.getOidByNameAndCid(officeName,cid);
    }
}
