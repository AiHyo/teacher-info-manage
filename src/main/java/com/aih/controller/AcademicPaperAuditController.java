package com.aih.controller;

import com.aih.utils.vo.R;
import com.aih.entity.AcademicPaperAudit;
import com.aih.service.IAcademicPaperAuditService;
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
     * tid、createTime、auditStatus、isShow以及删除标记会自动填充
     */
    @ApiOperation(value = "提交论文审核")
    @PostMapping("/submit")
    public R<?> saveAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaperService.save(academicPaper);
        return R.success("提交成功");
    }

    /**
     * 非法操作抛出自定义异常
     */
    @ApiOperation(value = "根据id查询论文审核信息")
    @GetMapping("/query/{id}")
    public R<AcademicPaperAudit> queryById(@PathVariable("id") Long id) {
        return R.success(academicPaperService.queryById(id));
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充 非法操作抛出自定义异常
     */
    @ApiOperation(value = "通过论文审核")
    @PutMapping("/pass")
    public R<?> passAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaperService.passAcademicPaperAudit(academicPaper);
        return R.success("审核通过");
    }

    /**
     * 审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充
     */
    @ApiOperation(value = "驳回论文审核")
    @PutMapping("/reject")
    public R<?> rejectAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        academicPaperService.rejectAcademicPaperAudit(academicPaper);
        return R.success("审核驳回");
    }


    /**
     * 根据携带的token查询
     */
    @ApiOperation(value = "查询自己的审核信息")
    @GetMapping("/queryOwnAll")
    public R<List<AcademicPaperAudit>> queryOwnAll(){
        List<AcademicPaperAudit> list = academicPaperService.queryOwnAll();
        return R.success(list);
    }

    @ApiOperation(value = "查询自己未审核的")
    @GetMapping("/queryOwnNotAudit")
    public R<List<AcademicPaperAudit>> queryOwnNotAudit(){
        List<AcademicPaperAudit> list = academicPaperService.queryOwnNotAudit();
        return R.success(list);
    }

    @ApiOperation(value = "查询自己审核通过的")
    @GetMapping("/queryOwnPassAudit")
    public R<List<AcademicPaperAudit>> queryOwnPassAudit(){

        List<AcademicPaperAudit> list = academicPaperService.queryOwnPassAudit();
        return R.success(list);
    }

    @ApiOperation(value = "查询自己审核驳回的")
    @GetMapping("/queryOwnRejectAudit")
    public R<List<AcademicPaperAudit>> queryOwnRejectAudit(){
        List<AcademicPaperAudit> list = academicPaperService.queryOwnRejectAudit();
        return R.success(list);
    }

    //通用
    /**
     * 通用 删除自己的显示列表（角色间互不干扰） 特判：本人删除自己未审核记录，即删除审核申请，直接执行删除操作（审核/管理也删除）
     * 另外 没有权利 / 已删除过 会返回自定义错误
     */
    @ApiOperation(value = "删除审核记录")//添加删除角色
    @DeleteMapping("/deleteRecord/{id}")
    public R<?> addDeleteRoles(@PathVariable("id") Long id){
        academicPaperService.addDeleteRoles(id);
        return R.success("删除成功");
    }

    //管理员
    /**
     * 管理员：审核员：根据个人携带的token，查询自己上任后手下的审核信息
     */
    @ApiOperation(value = "[管理员]查询学院下所有审核员审核信息")
    @GetMapping("/queryAllByCid")
    public R<List<AcademicPaperAudit>> queryAllByCid() {
        List<AcademicPaperAudit> list = academicPaperService.queryAllByCid();
        return R.success(list);
    }
    @ApiOperation(value = "查询学院下未审批的")
    @GetMapping("/queryNotAuditByCid")
    public R<List<AcademicPaperAudit>> queryNotAuditByCid() {
        List<AcademicPaperAudit> list = academicPaperService.queryNotAuditByCid();
        return R.success(list);
    }
    @ApiOperation(value = "查询学院下审核通过的")
    @GetMapping("/queryPassAuditByCid")
    public R<List<AcademicPaperAudit>> queryPassAuditByCid() {
        List<AcademicPaperAudit> list = academicPaperService.queryPassAuditByCid();
        return R.success(list);
    }
    @ApiOperation(value = "查询学院下审核驳回的")
    @GetMapping("/queryRejectAuditByCid")
    public R<List<AcademicPaperAudit>> queryRejectAuditByCid() {
        List<AcademicPaperAudit> list = academicPaperService.queryRejectAuditByCid();
        return R.success(list);
    }


    //审核员
    /**
     * 审核员：根据个人携带的token，查询自己上任后手下的审核信息
     */
    @ApiOperation(value = "[审核员]查询教研室下所有教师审核信息")
    @GetMapping("/queryAllByOid")
    public R<List<AcademicPaperAudit>> queryAllByOid() {
        List<AcademicPaperAudit> list = academicPaperService.queryAllByOid();
        return R.success(list);
    }
    @ApiOperation(value = "查询教研室下未审批的")
    @GetMapping("/queryNotAuditByOid")
    public R<List<AcademicPaperAudit>> queryNotAuditByOid() {
        List<AcademicPaperAudit> list = academicPaperService.queryNotAuditByOid();
        return R.success(list);
    }
    @ApiOperation(value = "查询教研室下审核通过的")
    @GetMapping("/queryPassAuditByOid")
    public R<List<AcademicPaperAudit>> queryPassAuditByOid() {
        List<AcademicPaperAudit> list = academicPaperService.queryPassAuditByOid();
        return R.success(list);
    }
    @ApiOperation(value = "查询教研室下审核驳回的")
    @GetMapping("/queryRejectAuditByOid")
    public R<List<AcademicPaperAudit>> queryRejectAuditByOid() {
        List<AcademicPaperAudit> list = academicPaperService.queryRejectAuditByOid();
        return R.success(list);
    }


}
