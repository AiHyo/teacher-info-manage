package com.aih.controller;

import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.WorkExperienceAudit;
import com.aih.service.IWorkExperienceAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作经历审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/workExperienceAudit")
public class WorkExperienceAuditController {

    @Autowired
    private IWorkExperienceAuditService workExperienceService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation("提交工作经历审核")
    @PostMapping("/submit")
    public R<?> saveWorkExperienceAudit(@RequestBody WorkExperienceAudit workExperience){
        workExperienceService.save(workExperience);
        return R.success("提交成功");
    }
    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过工作经历审核")
    @PostMapping("/pass")
    public R<?> passWorkExperienceAudit(@RequestBody WorkExperienceAudit workExperience){
        workExperience.setAuditStatus(1);//审核通过
        workExperience.setIsShow(1);//审核通过后自动显示
        workExperienceService.updateById(workExperience);
        return R.success("审核通过");
    }
    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回工作经历审核")
    @PostMapping("/reject")
    public R<?> rejectWorkExperienceAudit(@RequestBody WorkExperienceAudit workExperience){
        workExperience.setAuditStatus(2);//审核驳回
        workExperienceService.updateById(workExperience);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的工作经历审核信息列表
     */
    @ApiOperation(value = "查询自己的工作经历审核信息")
    @GetMapping("/queryOwn")
    public R<List<WorkExperienceAudit>> queryOwnWorkExperienceAudit() {
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkExperienceAudit::getTid, UserInfoContext.getTeacher().getId());
        List<WorkExperienceAudit> list = workExperienceService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询工作经历审核信息
     */
    @ApiOperation(value = "根据学院id查询工作经历审核信息")
    @GetMapping("/queryByCid")
    public R<List<WorkExperienceAudit>> queryByCid() {
        List<WorkExperienceAudit> list = workExperienceService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询工作经历审核信息
     */
    @ApiOperation(value = "根据教研室id查询工作经历审核信息")
    @GetMapping("/queryByOid")
    public R<List<WorkExperienceAudit>> queryByOid() {
        List<WorkExperienceAudit> list = workExperienceService.queryByOid();
        return R.success(list);
    }

}
