package com.aih.controller;

import com.aih.entity.vo.OfficeVo;
import com.aih.service.IOfficeService;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R<Page<OfficeVo>> getAllOffice(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam(value = "officeName",required = false) String officeName,
                                          @RequestParam(value = "cid", required = false)Long cid){
        return R.success(officeService.getAllOffice(pageNum,pageSize,officeName,cid));
    }
}
