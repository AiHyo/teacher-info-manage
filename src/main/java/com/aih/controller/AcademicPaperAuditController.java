package com.aih.controller;

import cn.hutool.core.io.FileUtil;
import com.aih.custom.annotation.AuthAccess;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.entity.Teacher;
import com.aih.mapper.TeacherMapper;
import com.aih.service.ITeacherService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.FileInfo;
import com.aih.utils.vo.R;
import com.aih.entity.AcademicPaperAudit;
import com.aih.service.IAcademicPaperAuditService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 论文审核 Controller
 *
 * @author AiH
 * @since 2023-07-09
 */
@Slf4j
@RestController
@RequestMapping("/academicPaperAudit")
//@ComponentScan(basePackages = {"com.aih.utils"})
public class AcademicPaperAuditController {

    @Autowired
    private MyUtil myUtil;
    @Autowired
    private IAcademicPaperAuditService academicPaperService;
    @Resource
    private TeacherMapper teacherMapper;

    /**
     * tid、createTime、auditStatus、isShow以及删除标记会自动填充
     */
    @ApiOperation(value = "提交论文审核")
    @PostMapping("/submit")
    public R<?> saveAcademicPaperAudit(@RequestBody AcademicPaperAudit academicPaper){
        //需要上传附件
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
     * 教师：根据个人携带的token，查询自己的审核信息
     * @param auditStatus 0待审核 1审核通过 2审核驳回 （可选 不选默认全部）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param title 标题 （可选）
     */
    @ApiOperation(value = "[教师]查询自己审核记录")
    @GetMapping("/queryOwnRecord")
    public R<Page<AcademicPaperAudit>> queryOwnRecord(@RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                                   @RequestParam("pageNum") Integer pageNum,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   @RequestParam(value = "title",required = false) String title){
        if (UserInfoContext.getUser().getId().toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        Page<AcademicPaperAudit> pageInfo = new Page<>(pageNum, pageSize);
        return R.success(academicPaperService.queryOwnRecord(pageInfo,auditStatus,title));
    }


    //通用
    /**
     * @param id 记录id
     * @return
     * 通用 删除自己的显示列表（角色间互不干扰） 特判：本人删除自己未审核记录，即删除审核申请，直接执行删除操作（审核/管理也删除）
     * 另外 没有权利 / 已删除过 会返回自定义错误
     */
    @ApiOperation(value = "删除审核记录")//添加删除角色
    @DeleteMapping("/deleteRecord/{id}")
    public R<?> addDeleteRoles(@PathVariable("id") Long id){
        academicPaperService.addDeleteRoles(id);
        return R.success("删除成功");
    }

    //审核员
    /**
     * 审核员：根据个人携带的token，查询自己上任后手下的审核信息
     */
    @ApiOperation(value = "[审核员]查询教研室下所有教师审核信息")
    @GetMapping("/queryRecordsByOid")
    public R<Page<AcademicPaperAudit>> queryRecordsByOid(@RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                                     @RequestParam("pageNum") Integer pageNum,
                                                     @RequestParam("pageSize") Integer pageSize,
                                                     @RequestParam(value = "title",required = false) String title) {
        Long uid = UserInfoContext.getUser().getId();
        if (uid.toString().charAt(0) != '1' || teacherMapper.selectById(uid).getIsAuditor()!=1){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_AUDITOR);
        }
        Page<AcademicPaperAudit> pageInfo = new Page<>(pageNum, pageSize);
        return R.success(academicPaperService.queryRecordsByOid(pageInfo,auditStatus,title));
    }

    //管理员
    /**
     * 管理员：审核员：根据个人携带的token，查询自己上任后手下的审核信息
     */
    @ApiOperation(value = "[管理员]查询学院下所有审核员审核信息")
    @GetMapping("/queryAllByCid")
    public R<Page<AcademicPaperAudit>> queryAllByCid(@RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                                     @RequestParam("pageNum") Integer pageNum,
                                                     @RequestParam("pageSize") Integer pageSize,
                                                     @RequestParam(value = "title",required = false) String title){
        if (UserInfoContext.getUser().getId().toString().charAt(0) != '2'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
        }
        Page<AcademicPaperAudit> pageInfo = new Page<>(pageNum, pageSize);
        return R.success(academicPaperService.queryRecordsByCid(pageInfo,auditStatus,title));
    }



    //////////////////////////////附件////////////////////////////
    @Value("${ip:localhost}")
    String ip;
    @Value("${server.port}")
    String port;
    @Value("${file.root-path}")
    String rootPath;
    String basePath;
    @PostConstruct
    public void init() {
        basePath = rootPath + File.separator + "academicPaper";
    }

    //用户
    @ApiOperation("上传附件")
    @PostMapping("/file/upload")
    public R<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
//        basePath.replace("\\","/");
        Long tid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(tid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        String parentPath =  basePath + File.separator + tid;
        String fileName = file.getOriginalFilename(); // 文件全名
        //abc.png
        String mainName = FileUtil.mainName(fileName); // abc 文件名
        String extName = FileUtil.extName(fileName);   // png 文件后缀
        if (!FileUtil.exist(parentPath)) {
            FileUtil.mkdir(parentPath); // 创建文件根目录
        }
        // 如果当前上传的文件已经存在了，那么这个时候我就要重名一个文件名称
        if (FileUtil.exist(parentPath + File.separator + fileName)) {
            fileName = System.currentTimeMillis() + "_" + mainName + "." + extName;
          //fileName = System.currentTimeMillis() + "_" + fileName;
        }
        File saveFile = new File(parentPath + File.separator + fileName);
        file.transferTo(saveFile); //将文件上传至对应磁盘位置
        String url = "http://" + ip + ":" + port + "/academicPaperAudit/file/download/" + tid + "/" + fileName;
        return R.success(url);
    }

    /**
     * 下载附件 无拦截
     */
    @AuthAccess
    @ApiOperation("下载附件")
    @GetMapping("/file/download/{tid}/{fileName}")
    public void download(@PathVariable("tid")Long tid, @PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        String parentPath =  basePath + File.separator + tid;
/*
      response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = response.getOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(os)
        ) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        }*/
        String filePath = parentPath + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            log.error("文件不存在：{}", filePath);
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        //对文件名编码是解决中文文件名乱码问题
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 下载
        //response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 预览
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    //自己
    /**
     * 用户重命名的时候,如果重名继续会覆盖原文件
     */
    @ApiOperation("自己是否有该文件存在")
    @GetMapping("/file/isExist/{fileName}")
    public R<?> isExist(@PathVariable("fileName") String fileName) {
        Long uid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(uid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        String parentPath =  basePath + File.separator + uid;
        String filePath = parentPath + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {//如果不存在
            return R.success(false);
        }
        return R.success(true);
    }
    //自己
    /**
     * 重命名文件,如果重名会覆盖,请先调用判断
     * @param oldFileName 原文件名
     * @param newMainName 新名称 ：不带后缀
     */
    @ApiOperation("重命名文件")
    @PutMapping("/file/rename")
    public R<?> rename(@RequestParam String oldFileName,@RequestParam String newMainName)
    {
        Long uid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(uid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }

        String parentPath =  basePath + File.separator + uid;
        String filePath = parentPath + File.separator + oldFileName;
        if (!FileUtil.exist(filePath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.rename(new File(filePath),newMainName,true,true);//重命名文件如果存在则覆盖
        return R.success("重命名成功");
    }

    /**
     * 自己才能删除自己的附件
     */
    @ApiOperation("删除自己原附件文件夹")
    @DeleteMapping("/file/deleteAll")
    public R<?> delete(){
        Long tid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(tid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        String parentPath =  basePath + File.separator + tid;
        if (!FileUtil.exist(parentPath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.del(parentPath);//删除文件夹以及文件夹下的文件
        return R.success("删除原附件成功");
    }
    @ApiOperation("删除自己的附件文件")
    @DeleteMapping("/file/delete/{fileName}")
    public R<?> delete(@PathVariable("fileName") String fileName){
        Long uid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(uid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        String parentPath =  basePath + File.separator + uid;
        String filePath = parentPath + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.del(filePath);//删除文件
        //如果同级目录没有文件了，则删除父级文件夹
        if(FileUtil.listFileNames(parentPath).isEmpty()){
            FileUtil.del(parentPath);
        }

        return R.success("删除附件成功");
    }

    /**
     * 是否有文件夹,有则说明已有附件
     */
    @ApiOperation("判断自己是否已有附件")
    @GetMapping("/file/isExist")
    public R<?> isExist() {
        Long uid = UserInfoContext.getUser().getId();
        //判断是否是教师用户
        if(uid.toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        String parentPath =  basePath + File.separator + uid;
        if (!FileUtil.exist(parentPath)) {//如果不存在
            return R.success(false);
        }
        return R.success(true);
    }

    /**
     * 需要角色有审核权限才能查看
     */
    @ApiOperation("根据tid查询附件列表")
    @GetMapping("/file/list/{tid}") //附件不会很多,应该不需要分页
    public R<List<FileInfo>> list(@PathVariable("tid") Long tid) {
        //①tid合法:判断tid是否教师用户
        if (tid.toString().charAt(0) != '1') {
            throw new CustomException(CustomExceptionCodeMsg.PATH_PARAM_ILLEGAL);
        }
        //②tid合法:判断uid是否有权限
        Long uid = UserInfoContext.getUser().getId();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(uid)) {
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }

        String parentPath = basePath + File.separator + tid;
        if (!FileUtil.exist(parentPath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<File> files = FileUtil.loopFiles(parentPath);//获取文件夹下的所有文件
        List<FileInfo> fileList = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            long fileLength = file.length();
            String fileSize = FileUtil.readableFileSize(fileLength);//文件大小转换成对应的单位
            String lastModifiedTime  = format.format(file.lastModified());//时间戳转换成对应格式的时间
            FileInfo fileInfo = new FileInfo(fileName, fileLength, fileSize, lastModifiedTime);
            fileList.add(fileInfo);
        }
        return R.success(fileList);
    }


}
