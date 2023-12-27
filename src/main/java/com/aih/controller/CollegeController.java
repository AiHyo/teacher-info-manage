package com.aih.controller;

import com.aih.entity.College;
import com.aih.service.ICollegeService;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学院 前端控制器
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Api(tags = "学院管理")
@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private ICollegeService collegeService;

    @ApiOperation("获取所有学院")
    @GetMapping("/getAllCollege")
    public R<Page<College>> getAllCollege(@RequestParam("pageNum")Integer pageNum, @RequestParam("pageSize")Integer pageSize,
                                          @RequestParam(value = "collegeName",required = false)String collegeName){
        return R.success(collegeService.getAllCollege(pageNum,pageSize,collegeName));
    }


}
