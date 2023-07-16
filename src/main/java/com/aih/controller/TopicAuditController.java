package com.aih.controller;

import com.aih.entity.TopicAudit;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.TopicAudit;
import com.aih.service.ITopicAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课题审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/topicAudit")
public class TopicAuditController {

    @Autowired
    private ITopicAuditService topicService;

    /**
     * tid、createTime、auditStatus、isShow会自动填充
     */
    @ApiOperation("提交课题审核")
    @PostMapping("/submit")
    public R<?> saveTopicAudit(@RequestBody TopicAudit topic){
        topicService.save(topic);
        return R.success("提交成功");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("通过课题审核")
    @PostMapping("/pass")
    public R<?> passTopicAudit(@RequestBody TopicAudit topic){
        topic.setAuditStatus(1);//审核通过
        topic.setIsShow(1);//审核通过后自动显示
        topicService.updateById(topic);
        return R.success("审核通过");
    }
    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation("驳回课题审核")
    @PostMapping("/reject")
    public R<?> rejectTopicAudit(@RequestBody TopicAudit topic){
        topic.setAuditStatus(2);//审核驳回
        topicService.updateById(topic);
        return R.success("审核驳回");
    }

    /**
     * 根据自带的token查询自己的课题审核信息列表
     */
    @ApiOperation(value = "查询自己的课题审核信息")
    @GetMapping("/queryOwn")
    public R<List<TopicAudit>> queryOwnTopicAudit() {
        LambdaQueryWrapper<TopicAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TopicAudit::getTid, UserInfoContext.getTeacher().getId());
        List<TopicAudit> list = topicService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据携带的token中学院id查询课题审核信息
     */
    @ApiOperation(value = "根据学院id查询课题审核信息")
    @GetMapping("/queryByCid")
    public R<List<TopicAudit>> queryByCid() {
        List<TopicAudit> list = topicService.queryByCid();
        return R.success(list);
    }

    /**
     * 根据携带的token中教研室id查询课题审核信息
     */
    @ApiOperation(value = "根据教研室id查询课题审核信息")
    @GetMapping("/queryByOid")
    public R<List<TopicAudit>> queryByOid() {
        List<TopicAudit> list = topicService.queryByOid();
        return R.success(list);
    }
}
