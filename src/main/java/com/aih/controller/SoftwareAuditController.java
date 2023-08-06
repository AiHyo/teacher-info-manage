package com.aih.controller;

import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.SoftwareAudit;
import com.aih.service.ISoftwareAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 软件著作审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/softwareAudit")
public class SoftwareAuditController {

    @Autowired
    private ISoftwareAuditService softwareService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation("提交软件著作审核")
    @PostMapping("/submit")
    public R<?> saveSoftwareAudit(@RequestBody SoftwareAudit software){
        softwareService.save(software);
        return R.success("提交成功");
    }

    @ApiOperation(value = "根据id查询审核信息")
    @GetMapping("/query/{id}")
    public R<SoftwareAudit> queryById(@PathVariable("id") Integer id) {
        return R.success(softwareService.getById(id));
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过软件著作审核")
    @PutMapping("/pass")
    public R<?> passSoftwareAudit(@RequestBody SoftwareAudit software){
        software.setAuditStatus(1);//审核通过
        software.setIsShow(1);//审核通过后自动显示
        softwareService.updateById(software);
        return R.success("审核通过");
    }
    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回软件著作审核")
    @PutMapping("/reject")
    public R<?> rejectSoftwareAudit(@RequestBody SoftwareAudit software){
        software.setAuditStatus(2);//审核驳回
        softwareService.updateById(software);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的软件著作审核信息列表
     */
    @ApiOperation(value = "查询自己的软件著作审核信息")
    @GetMapping("/queryOwn")
    public R<List<SoftwareAudit>> queryOwnSoftwareAudit() {
        LambdaQueryWrapper<SoftwareAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SoftwareAudit::getTid, UserInfoContext.getUser().getId());
        List<SoftwareAudit> list = softwareService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 根据携带的token中学院id查询软件著作审核信息
     */
    @ApiOperation(value = "根据学院id查询软件著作审核信息")
    @GetMapping("/queryByCid")
    public R<List<SoftwareAudit>> queryByCid() {
        List<SoftwareAudit> list = softwareService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询软件著作审核信息
     */
    @ApiOperation(value = "根据教研室id查询软件著作审核信息")
    @GetMapping("/queryByOid")
    public R<List<SoftwareAudit>> queryByOid() {
        List<SoftwareAudit> list = softwareService.queryByOid();
        return R.success(list);
    }

}
