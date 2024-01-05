package com.aih.controller;

import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.vo.TeacherDto;
import com.aih.entity.vo.audit.AuditInfoDto;
import com.aih.service.IAdminService;
import com.aih.service.ITeacherService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.utils.vo.RoleType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preview")
public class PreviewController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private ITeacherService teacherService;
    /**
     * 查询的是所有类型的信息,统一部分内容检验auditStatus是否合法,不合法抛出自定义信息
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @param auditStatus (可选)审核状态,只接受0/1/2,不传则查询所有
     * @param onlyOwn 布尔类型(可选)只看自己的,默认false
     * @return 每条数据包含：审核类型、审核对象id、审核状态、创建时间、审核时间、教师姓名、教研室名称、学院名称
     */
    @ApiOperation("[管理员/审核员][预览]查询管理学院下所有的审核员审核记录")
    @GetMapping("/getAuditList")
    public R<Page<AuditInfoDto>> getAuditList(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(value = "auditStatus", required = false) Integer auditStatus,
                                              @RequestParam(value = "onlyOwn",required = false,defaultValue = "false")boolean onlyOwn) {
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if (roleType != RoleType.ADMIN && roleType != RoleType.AUDITOR) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);//检查auditStatus参数是否合法
        }
        if (roleType == RoleType.ADMIN) {
            return R.success(adminService.getAuditList(pageNum, pageSize, auditStatus, onlyOwn));
        }//else AUDITOR
        return R.success(teacherService.getAuditList(pageNum, pageSize, auditStatus, onlyOwn));

    }

    /**
     * 可选模糊查询 keyword: 教研室/教师名称,其它都是对应教师属性的模糊查询.其中isAuditor/officeName/oid/keyword只有管理员传参才生效
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param keyword 教研室/教师名称
     * @param oid 办公室id
     * @param officeName 教研室名称
     * @param teacherName 教师名称
     * @param isAuditor 是否为审核员,0否1是,不传则查询所有
     * @param gender 性别
     * @param ethnic 民族
     * @param birthplace 籍贯
     * @param address 住址
     * @return
     */
    @ApiOperation("[管理员/审核员]查询权限下的教师")
    @GetMapping("/getTeacherList")
    public R<Page<TeacherDto>> admin_GetTeacherList(@RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "oid", required = false) Long oid,
                                                    @RequestParam(value = "officeName", required = false) String officeName,
                                                    @RequestParam(value = "teacherName", required = false) String teacherName,
                                                    @RequestParam(value = "isAuditor", required = false) Integer isAuditor,
                                                    @RequestParam(value = "gender", required = false) Integer gender,
                                                    @RequestParam(value = "ethnic", required = false) String ethnic,
                                                    @RequestParam(value = "birthplace", required = false) String birthplace,
                                                    @RequestParam(value = "address", required = false) String address) {
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if (roleType != RoleType.ADMIN && roleType != RoleType.AUDITOR) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        if (roleType == RoleType.ADMIN) {
            return R.success(adminService.getTeacherList(pageNum, pageSize, keyword, oid, officeName, teacherName, isAuditor, gender, ethnic, birthplace, address));
        }//else AUDITOR
        return R.success(teacherService.getTeacherList(pageNum, pageSize, teacherName, gender, ethnic, birthplace, address));
    }

}
