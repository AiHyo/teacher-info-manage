package com.aih.controller;

import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.IdentityCardAudit;
import com.aih.service.IIdentityCardAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 身份证审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/identityCardAudit")
public class IdentityCardAuditController {

    @Autowired
    private IIdentityCardAuditService identityCardService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation("提交身份证审核")
    @PostMapping("/submit")
    public R<?> saveIdentityCardAudit(@RequestBody IdentityCardAudit identityCard){
        identityCardService.save(identityCard);
        return R.success("提交成功");
    }

    @ApiOperation(value = "根据id查询审核信息")
    @GetMapping("/query/{id}")
    public R<IdentityCardAudit> queryById(@PathVariable("id") Integer id) {
        return R.success(identityCardService.getById(id));
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过身份证审核")
    @PutMapping("/pass")
    public R<?> passIdentityCardAudit(@RequestBody IdentityCardAudit identityCard){
        identityCard.setAuditStatus(1);//审核通过
        identityCard.setIsShow(1);//审核通过后自动显示
        identityCardService.updateById(identityCard);
        return R.success("审核通过");
    }


    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回身份证审核")
    @PutMapping("/reject")
    public R<?> rejectIdentityCardAudit(@RequestBody IdentityCardAudit identityCard){
        identityCard.setAuditStatus(2);//审核驳回
        identityCardService.updateById(identityCard);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的身份证审核信息列表
     */
    @ApiOperation(value = "查询自己的身份证审核信息")
    @GetMapping("/queryOwn")
    public R<List<IdentityCardAudit>> queryOwnIdentityCardAudit() {
        LambdaQueryWrapper<IdentityCardAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdentityCardAudit::getTid, UserInfoContext.getUser().getId());
        queryWrapper.eq(IdentityCardAudit::getTid, UserInfoContext.getUser().getId());
        List<IdentityCardAudit> list = identityCardService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询身份证审核信息
     */
    @ApiOperation(value = "根据学院id查询身份证审核信息")
    @GetMapping("/queryByCid")
    public R<List<IdentityCardAudit>> queryByCid() {
        List<IdentityCardAudit> list = identityCardService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询身份证审核信息
     */
    @ApiOperation(value = "根据教研室id查询身份证审核信息")
    @GetMapping("/queryByOid")
    public R<List<IdentityCardAudit>> queryByOid() {
        List<IdentityCardAudit> list = identityCardService.queryByOid();
        return R.success(list);
    }
}
