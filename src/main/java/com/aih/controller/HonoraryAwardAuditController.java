package com.aih.controller;

import com.aih.entity.HonoraryAwardAudit;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.HonoraryAwardAudit;
import com.aih.service.IHonoraryAwardAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 荣誉奖项审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/honoraryAwardAudit")
public class HonoraryAwardAuditController {

    @Autowired
    private IHonoraryAwardAuditService honoraryAwardService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation(value = "提交荣誉奖项审核")
    @PostMapping("/submit")
    public R<?> saveHonoraryAwardAudit(@RequestBody HonoraryAwardAudit honoraryAward){
        honoraryAwardService.save(honoraryAward);
        return R.success("提交成功");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过荣誉奖项审核")
    @PutMapping("/pass")
    public R<?> passHonoraryAwardAudit(@RequestBody HonoraryAwardAudit honoraryAward){
        honoraryAward.setAuditStatus(1);//审核通过
        honoraryAward.setIsShow(1);//审核通过后自动显示
        honoraryAwardService.updateById(honoraryAward);
        return R.success("审核通过");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回荣誉奖项审核")
    @PutMapping("/reject")
    public R<?> rejectHonoraryAwardAudit(@RequestBody HonoraryAwardAudit honoraryAward){
        honoraryAward.setAuditStatus(2);//审核驳回
        honoraryAwardService.updateById(honoraryAward);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的荣誉奖项审核信息列表
     */
    @ApiOperation(value = "查询自己的荣誉奖项审核信息")
    @GetMapping("/queryOwn")
    public R<List<HonoraryAwardAudit>> queryOwnHonoraryAwardAudit() {
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HonoraryAwardAudit::getTid, UserInfoContext.getTeacher().getId());
        List<HonoraryAwardAudit> list = honoraryAwardService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询荣誉奖项审核信息
     */
    @ApiOperation(value = "根据学院id查询荣誉奖项审核信息")
    @GetMapping("/queryByCid")
    public R<List<HonoraryAwardAudit>> queryByCid() {
        List<HonoraryAwardAudit> list = honoraryAwardService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询荣誉奖项审核信息
     */
    @ApiOperation(value = "根据教研室id查询荣誉奖项审核信息")
    @GetMapping("/queryByOid")
    public R<List<HonoraryAwardAudit>> queryByOid() {
        List<HonoraryAwardAudit> list = honoraryAwardService.queryByOid();
        return R.success(list);
    }

}
