package com.aih.utils;

import cn.hutool.core.io.FileUtil;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Admin;
import com.aih.entity.College;
import com.aih.entity.Teacher;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.CollegeMapper;
import com.aih.mapper.OfficeMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.utils.vo.PoliticsStatus;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Component
public class MyUtil {

    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private OfficeMapper officeMapper;

    public static void checkPoliticsStatus(String politicsStatus) {
        if (politicsStatus==null){
            return;
        }
        try {
            PoliticsStatus.valueOf(politicsStatus);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomExceptionCodeMsg.POLITICS_STATUS_ILLEGAL);
        }
    }

    /**
     * 获取审核员有权利 审核 的tids
     */
    public List<Long> getAuditorCanAuditTids(){
        //获取oid：审核员的管理办公室
        Long uid = UserInfoContext.getUser().getId();
        Teacher teacher = teacherMapper.selectById(uid);
        Long oid = teacher.getOid();
        List<Long> canAuditTids = teacherMapper.getCanAuditTidsByOid(oid);
        log.info("审核员有权利审核的tid:{}", canAuditTids);
        return canAuditTids;
    }

    /**
     *  获取管理员有权利 审核 的tids
     */
    public List<Long> getAdminCanAuditTids() {
        //获取cid：管理员的管理学院
        Long uid = UserInfoContext.getUser().getId();
        Admin admin = adminMapper.selectById(uid);
        Long cid = admin.getCid();
        List<Long> canAuditTids = teacherMapper.getCanAuditTidsByCid(cid);
        log.info("管理员有权利审核的tid:{}", canAuditTids);
        return canAuditTids;
    }

    /**
     * 判断是否是教师
     */
    public static void checkIsTeacher(){
        Long uid = UserInfoContext.getUser().getId();
        //uid的开头为1
        if (uid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
    }

    // ============== 判断tid是否存在并返回Teacher ==============
    public Teacher checkTidExistAndReturnTeacher(Long tid){
        Teacher teacher = teacherMapper.selectById(tid);
        if (teacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        return teacher;
    }
    // ============== 判断cid是否存在并返回College ==============
    public College checkCidExistAndReturnCollege(Long cid){
        College college = collegeMapper.selectById(cid);
        if (college == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_COLLEGE);
        }
        return college;
    }

    /**
     * 获取有权利查看tid审核信息的上级ids,注：包括tid自己
     */
    public List<Long> getPowerIdsByTid(Long tid){
        Teacher teacher = teacherMapper.selectById(tid);//获取该记录的所属教师
        if (teacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        Integer isAuditor = teacher.getIsAuditor();
        //获取有权限操作该条记录的id
        List<Long> powerIds = null;
        if (0 == isAuditor) {
            //非审核员(=是教师),则根据教研室id,查审核员id
            Long oid = teacher.getOid();
            powerIds = teacherMapper.getAuditorPowerIdsByOid(oid);
            log.info("该记录有权限的审核员id:{}", powerIds);
        }else {
            //是审核员,则根据学院id,查管理员id
            Long cid = teacher.getCid();
            powerIds = adminMapper.getAdminIdsByCid(cid);
            log.info("该记录有权限的管理员id:{}", powerIds);
        }
        powerIds.add(tid);//再加上教师自己的id
        return powerIds;
    }

    //根据tid和uid判断有没有权限审核该记录
    public void checkAuditPower(Long tid){
        Long uid = UserInfoContext.getUser().getId();
        List<Long> powerIds = this.getPowerIdsByTid(tid);
        powerIds.remove(tid);//所属教师没有权限审核自己
        if (!powerIds.contains(uid)){//是否包含uid
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_AUDIT);
        }
    }

    /**
     * 以流的形式下载文件到客户端
     */
    public static void downloadZip(File file, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        byte[] bytes = FileUtil.readBytes(file);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    /**
     *  检查auditStatus参数是否合法
     */
    public static void checkAuditStatus(Integer auditStatus){
        if (auditStatus==null){
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_STATUS_ILLEGAL);
        }
        if (auditStatus != 0 && auditStatus != 1 && auditStatus != 2){
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_STATUS_ILLEGAL);
        }
    }

    //检验手机号格式是否正确
    public static void checkPhone(String phone){
        if(StringUtils.isBlank(phone)){
            throw new CustomException(CustomExceptionCodeMsg.PHONE_IS_EMPTY);
        }
        if (!phone.matches("^1[3-9]\\d{9}$")){
            throw new CustomException(CustomExceptionCodeMsg.PHONE_FORMAT_ERROR);
        }
    }


}
