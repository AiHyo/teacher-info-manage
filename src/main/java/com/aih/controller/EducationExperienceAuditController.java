package com.aih.controller;

import com.aih.entity.EducationExperienceAudit;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.service.IEducationExperienceAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教育经历审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/educationExperienceAudit")
public class EducationExperienceAuditController {

    @Autowired
    private IEducationExperienceAuditService educationExperienceService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation(value = "提交教育经历审核")
    @PostMapping("/submit")
    public R<?> saveEducationExperienceAudit(@RequestBody EducationExperienceAudit educationExperience){
        educationExperienceService.save(educationExperience);
        return R.success("提交成功");
    }

    @ApiOperation(value = "根据id查询审核信息")
    @GetMapping("/query/{id}")
    public R<EducationExperienceAudit> queryById(@PathVariable("id") Integer id) {
        return R.success(educationExperienceService.getById(id));
    }


    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过教育经历审核")
    @PutMapping("/pass")
    public R<?> passEducationExperienceAudit(@RequestBody EducationExperienceAudit educationExperience){
        educationExperience.setAuditStatus(1);//审核通过
        educationExperience.setIsShow(1);//审核通过后自动显示
        educationExperienceService.updateById(educationExperience);
        return R.success("审核通过");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回教育经历审核")
    @PutMapping("/reject")
    public R<?> rejectEducationExperienceAudit(@RequestBody EducationExperienceAudit educationExperience){
        educationExperience.setAuditStatus(2);//审核驳回
        educationExperienceService.updateById(educationExperience);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的教育经历审核信息列表
     */
    @ApiOperation(value = "查询自己的教育经历审核信息")
    @GetMapping("/queryOwn")
    public R<List<EducationExperienceAudit>> queryOwnEducationExperienceAudit(){
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EducationExperienceAudit::getTid, UserInfoContext.getUser().getId());
        List<EducationExperienceAudit> list = educationExperienceService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询教育经历审核信息
     */
    @ApiOperation(value = "根据学院id查询教育经历审核信息")
    @GetMapping("/queryByCid")
    public R<List<EducationExperienceAudit>> queryByCid() {
        List<EducationExperienceAudit> list = educationExperienceService.queryByCid();
        return R.success(list);
    }
    @ApiOperation(value = "查询学院下未审批的")
    @GetMapping("/queryByCidAndAuditStatus")
    public R<List<EducationExperienceAudit>> queryByCidAndAuditStatus() {
        List<EducationExperienceAudit> list = educationExperienceService.queryByCidAndAuditStatus();
        return R.success(list);
    }



    /**
     * 根据携带的token中教研室id查询教育经历审核信息
     */
    @ApiOperation(value = "[审核员]查询教研室下所有教师审核信息")
    @GetMapping("/queryByOid")
    public R<List<EducationExperienceAudit>> queryByOid() {
        List<EducationExperienceAudit> list = educationExperienceService.queryByOid();
        return R.success(list);
    }


}
