package com.aih.controller;

import com.aih.entity.WorkExperienceAudit;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.AcademicPaperAudit;
import com.aih.service.IAcademicPaperAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论文审核 Controller
 *
 * @author AiH
 * @since 2023-07-09
 */
@RestController
@RequestMapping("/academicPaperAudit")
public class AcademicPaperAuditController {

    @Autowired
    private IAcademicPaperAuditService academicPaperService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation(value = "提交论文审核")
    @PostMapping("/submit")
    public R<?> saveAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaperService.save(academicPaper);
        return R.success("提交成功");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation(value = "通过论文审核")
    @PutMapping("/pass")
    public R<?> passAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaper.setAuditStatus(1);//审核通过
        academicPaper.setIsShow(1);//审核通过后自动显示
        academicPaperService.updateById(academicPaper);
        return R.success("审核通过");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation(value = "驳回论文审核")
    @PutMapping("/reject")
    public R<?> rejectAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaper.setAuditStatus(2);//审核驳回
        academicPaperService.updateById(academicPaper);
        return R.success("审核驳回");
    }


    /**
     * 根据自带的token查询自己的论文审核信息列表
     */
    @ApiOperation(value = "查询自己论文审核信息")
    @GetMapping("/queryOwn")
    public R<List<AcademicPaperAudit>> queryOwnAcademicPaperAudit(){
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AcademicPaperAudit::getTid, UserInfoContext.getTeacher().getId());
        List<AcademicPaperAudit> list = academicPaperService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 管理员：根据携带的token中学院id查询论文审核信息
     */
    @ApiOperation(value = "[管理员]查询学院下所有审核员审核信息")
    @GetMapping("/queryByCid")
    public R<List<AcademicPaperAudit>> queryByCid() {
        List<AcademicPaperAudit> list = academicPaperService.queryByCid();
        return R.success(list);
    }

    /**
     * 审核员：根据携带的token中教研室id查询论文审核信息
     */
    @ApiOperation(value = "[审核员]查询教研室下所有教师审核信息")
    @GetMapping("/queryByOid")
    public R<List<AcademicPaperAudit>> queryByOid() {
        List<AcademicPaperAudit> list = academicPaperService.queryByOid();
        return R.success(list);
    }




}
