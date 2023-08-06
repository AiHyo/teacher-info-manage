package com.aih.controller;

import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.ProjectAudit;
import com.aih.service.IProjectAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/projectAudit")
public class ProjectAuditController {

    @Autowired
    private IProjectAuditService projectService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation("提交项目审核")
    @PostMapping("/submit")
    public R<?> saveProjectAudit(@RequestBody ProjectAudit project){
        projectService.save(project);
        return R.success("提交成功");
    }

    @ApiOperation(value = "根据id查询审核信息")
    @GetMapping("/query/{id}")
    public R<ProjectAudit> queryById(@PathVariable("id") Integer id) {
        return R.success(projectService.getById(id));
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过项目审核")
    @PutMapping("/pass")
    public R<?> passProjectAudit(@RequestBody ProjectAudit project){
        project.setAuditStatus(1);//审核通过
        project.setIsShow(1);//审核通过后自动显示
        projectService.updateById(project);
        return R.success("审核通过");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回项目审核")
    @PutMapping("/reject")
    public R<?> rejectProjectAudit(@RequestBody ProjectAudit project){
        project.setAuditStatus(2);//审核驳回
        projectService.updateById(project);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的项目审核信息列表
     */
    @ApiOperation(value = "查询自己的项目审核信息")
    @GetMapping("/queryOwn")
    public R<List<ProjectAudit>> queryOwnProjectAudit() {
        LambdaQueryWrapper<ProjectAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectAudit::getTid, UserInfoContext.getUser().getId());
        List<ProjectAudit> list = projectService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询项目审核信息
     */
    @ApiOperation(value = "根据学院id查询项目审核信息")
    @GetMapping("/queryByCid")
    public R<List<ProjectAudit>> queryByCid() {
        List<ProjectAudit> list = projectService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询项目审核信息
     */
    @ApiOperation(value = "根据教研室id查询项目审核信息")
    @GetMapping("/queryByOid")
    public R<List<ProjectAudit>> queryByOid() {
        List<ProjectAudit> list = projectService.queryByOid();
        return R.success(list);
    }

}
