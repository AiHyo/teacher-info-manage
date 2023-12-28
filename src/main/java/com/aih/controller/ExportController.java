package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.common.aop_log.LogAnnotation;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Office;
import com.aih.entity.Teacher;
import com.aih.mapper.OfficeMapper;
import com.aih.service.IAdminService;
import com.aih.service.ITeacherService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.RoleType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deepoove.poi.XWPFTemplate;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/export")
public class ExportController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private OfficeMapper officeMapper;
    @Resource //@Autowired
    private HttpServletResponse response;

    private final String sep = File.separator;
    @Value("${file.root-path}")
    String rootPath;
    @Value("${file.temporary-path}")
    String temporaryPath;
    @Value("${excel.file-name}")
    String defaultExcelName;


    //==========================================excel======================================
    /**
     * @param ids       （可选）
     * @param oids      （可选）教研室oids
     * @param fileName  （可选）导出excel的名称,默认"教师信息表"
     * @param fieldList 只导出部分字段（可选）id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
     * @throws IOException
     */

    /**
     * 管理员/审核员导出excel.
     * @param tids       （可选）
     * @param oids      （可选）教研室oids
     * @param fileName  （可选）导出excel的名称,默认"教师信息表"
     * @param fieldList 只导出部分字段（可选）id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
     * @throws IOException
     */
    @LogAnnotation(module = "管理员/审核员", operator = "Excel批量导出教师信息")
    @ApiOperation("Excel批量导出教师信息")
    @GetMapping(value = "/excel")
    public void exportExcel(@RequestParam(value = "tids", required = false) List<Long> tids,
                            @RequestParam(value = "oids", required = false) List<Long> oids,
                            @RequestParam(value = "fileName", required = false) String fileName,
                            @RequestParam(value = "fieldList", required = false) List<String> fieldList) throws IOException {
        if (StrUtil.isBlank(fileName)) {
            fileName = defaultExcelName;
        }
        List<Teacher> teacherList = null;
        if (UserInfoContext.getUser().getRoleType()== RoleType.ADMIN){
            teacherList = this.admin_GetTeacherList(tids, oids);
        }else{
            teacherList = this.auditor_GetTeacherList(tids);
        }
        // ===ExcelWriter:excel写入器===
        ExcelWriter writer = teacherService.getMyExcelWriter(teacherList, fileName, fieldList);
        //在浏览器下载：设置response并写出xlsx
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);//true 关闭输出流
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    //==========================================word======================================
    @ApiOperation("导出Word")
    @GetMapping("/word/{tid}")
    public void exportWord(@PathVariable Long tid) throws IOException {
        //判断权限
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if ((roleType == RoleType.ADMIN && !findTeacher.getCid().equals(UserInfoContext.getUser().getCid())
                || (roleType == RoleType.AUDITOR && !findTeacher.getOid().equals(UserInfoContext.getUser().getOid())))) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        //获取XWPFTemplate word写入器 渲染好数据的word
        XWPFTemplate render = teacherService.getWordRender(findTeacher);
        //在浏览器下载：设置response并写出docx
        String fileName = URLEncoder.encode(findTeacher.getTeacherName() + "教师信息表", "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".docx");
        ServletOutputStream outputStream = response.getOutputStream();
        render.writeAndClose(outputStream);
        render.close();//双重保障
        outputStream.flush();
        outputStream.close();
    }


    //============================================压缩包================================================

    /**
     * 可选参数限制导出内容,不选就默认所有
     * @param ids 教师id
     * @param oids 教研室id(管理员才生效)
     * @param fieldList      同Excel：id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
     * @param attachmentList 附件种类 (暂未定,先不填)
     */
    @ApiOperation("导出压缩包")
    @GetMapping("/pack/")
    @LogAnnotation(module="管理员",operator="导出压缩包")
    public void exportZip(@RequestParam(value = "ids", required = false) List<Long> ids,
                          @RequestParam(value = "oids", required = false) List<Long> oids,
                          @RequestParam(value = "fieldList", required = false) List<String> fieldList,
                          @RequestParam(value = "attachmentList", required = false) List<String> attachmentList) throws IOException {
        //创建一个新的临时文件路径！！！
        if (FileUtil.exist(temporaryPath)) {
            FileUtil.del(temporaryPath);
        }//创建临时zip包
        File tempZipFile = new File(temporaryPath + sep + "test压缩.zip");
        //需要压缩的文件列表
        List<File> fileList = CollUtil.newArrayList();
        //调用封装函数获取教师列表
        List<Teacher> teacherList = null;
        if (UserInfoContext.getUser().getRoleType()== RoleType.ADMIN){
            teacherList = this.admin_GetTeacherList(ids, oids);
        }else {
            teacherList = this.auditor_GetTeacherList(ids);
        }
        //添加需要压缩的文件
        fileList.add(teacherService.getTeacherAttachmentFolder(teacherList,attachmentList));//教师附件文件夹
        fileList.add(teacherService.getMyExcelFile(teacherList, fieldList)); //excel
        fileList.add(teacherService.getMyWordFolder(teacherList));           //word
        fileList.add(new File(rootPath + sep + "qwq.jpg"));
        //开始压缩
        ZipUtil.zip(tempZipFile, true, fileList.toArray(new File[fileList.size()]));
        //下载zip到浏览器！！！
        MyUtil.downloadZip(tempZipFile, response);
        //统一删除所有临时文件！！！！！！
        FileUtil.del(temporaryPath);
    }

    //==========================================封装方法=============================================
    //获取需要的teacherList
    private List<Teacher> admin_GetTeacherList(List<Long> tids, List<Long> oids) {
        Long u_cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        /*权限只有自己管理学院*/
        queryWrapper.eq(Teacher::getCid, u_cid);
        //根据传参判断选择的教师
        if (oids != null && !oids.isEmpty()) {//优先看oids
            for (Long oid : oids) {
                Office office = officeMapper.selectById(oid);
                if (office == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_OFFICE);
                }
                if (!office.getCid().equals(u_cid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法oid就根据oids选
            queryWrapper.in(Teacher::getOid, oids);
        } else if (tids != null && !tids.isEmpty()) {
            for (Long id : tids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
                }
                if (!teacher.getCid().equals(u_cid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法id就根据ids选
            queryWrapper.in(Teacher::getId, tids);
        } else log.info("没有选择指定教师,默认选择管理学院下所有教师");
        return teacherService.list(queryWrapper);
    }

    private List<Teacher> auditor_GetTeacherList(List<Long> tids) { // 传入ids,先检验,再获取teacherList
        Long u_oid = UserInfoContext.getUser().getOid();
//        Long u_oid = 1L;//测试用
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        /*权限只有自己管理学院*/
        queryWrapper.eq(Teacher::getOid, u_oid);
        //根据传参判断选择的教师
        if (tids != null && !tids.isEmpty()) {
            for (Long id : tids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
                }
                if (!teacher.getCid().equals(u_oid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法id就根据ids选
            queryWrapper.in(Teacher::getId, tids);
        } else log.info("没有选择指定教师,默认选择管理办公室下所有教师");
        return teacherService.list(queryWrapper);
    }

}
