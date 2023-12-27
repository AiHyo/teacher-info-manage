package com.aih.controller;

import com.aih.entity.vo.OfficeDto;
import com.aih.service.IOfficeService;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 教研室 前端控制器
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private IOfficeService officeService;
    //获取所有办公室
    @ApiOperation(value = "获取所有办公室")
    @GetMapping("/getAllOffice")
    public R<Page<OfficeDto>> getAllOffice(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam(value = "officeName",required = false) String officeName){
        return R.success(officeService.getAllOffice(pageNum,pageSize,officeName));
    }


}
