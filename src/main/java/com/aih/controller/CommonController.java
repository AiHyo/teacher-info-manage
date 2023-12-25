package com.aih.controller;

import com.aih.entity.Teacher;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.CollegeMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.utils.vo.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    //查询所属学院的公共接口
    @ApiOperation("查询所属学院")
    @GetMapping("/getCollege/{id}")
    public R<String> getCollege(@PathVariable("id") Long id){
        // 如果id是1开头
        if (id.toString().startsWith("1")){
            Teacher findTeacher = teacherMapper.selectById(id);
            Long cid = findTeacher.getCid();
            String collegeName = collegeMapper.getCollegeNameByCid(cid);
        }

        return R.success();
    }

}
