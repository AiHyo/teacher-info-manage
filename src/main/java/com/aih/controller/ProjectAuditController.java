package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.common.interceptor.AuthAccess;
import com.aih.entity.ProjectAudit;
import com.aih.entity.vo.audit.ProjectDto;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IProjectAuditService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.FileInfo;
import com.aih.utils.vo.R;
import com.aih.utils.vo.RoleType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 项目审核 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@Slf4j
@RestController
@RequestMapping("/projectAudit")
public class ProjectAuditController {

    @Autowired
    private MyUtil myUtil;
    @Autowired
    private IProjectAuditService projectService;
    @Resource
    private TeacherMapper teacherMapper;


    /**
     * tid、createTime、auditStatus、isShow、deleteRoles等无需填写,会自动填充
     * return 一个数字id,生成审核数据的id,用于调用上传附件接口
     */
    @ApiOperation(value = "[教师]提交项目审核")
    @PostMapping("/submit")
    public R<Long> saveProjectAudit(@RequestBody ProjectAudit project){
        if (UserInfoContext.getUser().getId().toString().charAt(0) != '1') {
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        projectService.save(project);
        Long id = project.getId(); // 返回id,用于调用上传附件接口
        return R.success(id);
    }


    /**
     * id不存在/没权限查看 ,返回自定义异常信息
     */
    @ApiOperation(value = "根据项目id查询具体信息")
    @GetMapping("/query/{id}")
    public R<ProjectDto> queryById(@PathVariable("id") Long id) {
        return R.success(projectService.queryDtoById(id)); //权限在service里验证
    }

    /**
     * 根据id通过项目审核,审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充 id不存在/已审核过/没审核权限会抛出自定义异常
     * @param id 记录id
     * @param auditorRemark 审核员备注(可选)
     */
    @ApiOperation(value = "通过项目审核")
    @PutMapping("/pass")
    public R<?> passProjectAudit(@RequestParam("id") Long id,@RequestParam(value = "auditorRemark",required = false) String auditorRemark){
        //判断id是否存在
        ProjectAudit findRecord = projectService.getById(id);
        if (findRecord == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        //判断权限
        myUtil.checkAuditPower(findRecord.getTid());
        //判断是否已经审核过
        if (findRecord.getAuditStatus() != 0){
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_ERROR_NOT_UNAUDIT);
        }
        //要不要加审核备注
        if (auditorRemark != null){
            findRecord.setAuditorRemark(auditorRemark);
        }
        projectService.passProjectAudit(findRecord);
//        this.makeFilesUsefulByID(id);
        return R.success("审核通过");
    }

    /**
     * 根据id驳回项目审核,审核员可添加审核员备注 审核员(aid)&审核时间(updateTime)会自动填充。id不存在/已审核过/没审核权限会抛出自定义异常
     * @param id 记录id
     * @param auditorRemark 审核员备注(可选)
     */
    @ApiOperation(value = "驳回项目审核")
    @PutMapping("/reject")
    public R<?> rejectProjectAudit(@RequestParam("id") Long id,@RequestParam(value = "auditorRemark",required = false) String auditorRemark){
        //判断id是否存在
        ProjectAudit findRecord = projectService.getById(id);
        if (findRecord == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        //判断权限
        myUtil.checkAuditPower(findRecord.getTid());
        //判断是否已经审核过
        if (findRecord.getAuditStatus() != 0){
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_ERROR_NOT_UNAUDIT);
        }
        //要不要加审核备注
        if (auditorRemark != null){
            findRecord.setAuditorRemark(auditorRemark);
        }
        projectService.rejectProjectAudit(findRecord);
        return R.success("审核驳回");
    }


    /**
     * 教师：根据个人携带的token，查询自己的审核信息
     * @param auditStatus 0待审核 1审核通过 2审核驳回 （可选 不选默认全部）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param keyword 模糊查询关键字(可选) 项目名称[projectName]
     */
    @ApiOperation(value = "[教师]查询自己审核记录")
    @GetMapping("/queryOwnRecord")
    public R<Page<ProjectDto>> queryOwnRecord(@RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                                          @RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam(value = "keyword",required = false) String keyword){
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);
        }
        if (UserInfoContext.getUser().getId().toString().charAt(0) != '1'){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
        }
        return R.success(projectService.queryOwnRecord(pageNum,pageSize,auditStatus, keyword));
    }


    //管理员/审核员
    /**
     * 管理员/审核员：根据个人携带的token，查询自己上任后手下的审核信息,按审核状态/时间排序。auditStatus不合法/不是审核者,会抛出自定义异常
     * @param auditStatus 0待审核 1审核通过 2审核驳回 （可选 不选默认全部）
     * @param onlyOwn 是否只看自己的(可选) 默认false
     * @param keyword 模糊查询关键字(可选) 项目名称[projectName]
     **/
    @ApiOperation(value = "[管理员/审核员]查询手下的审核信息")
    @GetMapping("/queryPowerRecords")
    public R<Page<ProjectDto>> queryPowerRecords(@RequestParam("pageNum") Integer pageNum,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                                 @RequestParam(value = "onlyOwn",required = false,defaultValue = "false") Boolean onlyOwn,
                                                 @RequestParam(value = "keyword",required = false) String keyword) {
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);
        }
        if (UserInfoContext.getUser().getRoleType() != RoleType.ADMIN && UserInfoContext.getUser().getRoleType() != RoleType.AUDITOR){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_AUDITOR);
        }
        return R.success(projectService.queryPowerRecords(pageNum,pageSize,auditStatus,onlyOwn,keyword));
    }

    //教师删除自己的审核通过记录,其实也就是修改isShow字段
    /**
     * 根据id删除自己的已生效,只有tid申请者可操作,直接执行修改isShow为0
     * 检验 id不存在/没有权限/已删除过 会返回自定义信息
     * @param id 记录id
     */
    @ApiOperation(value = "[教师]删除已生效信息")
    @DeleteMapping("/deleteOwnInfo/{id}")
    public R<?> deleteOwnInfo(@PathVariable("id") Long id){
        projectService.deleteOwnInfo(id);
        return R.success("删除已生效信息成功");
    }

    //通用
    /**
     * 根据id查找记录,若未审核,只有tid申请者可操作,直接执行逻辑删除(删除记录全部人都看不见)。
     * 若已审核,tid和审核者(申请者/审核者)都可操作,但都只是删除自己的记录(删除自己的显示列表),用户间互不干扰。
     * 检验 id不存在/没有权限/已删除过 会返回自定义信息
     * @param id 记录id
     * @return 根据情况返回'删除申请成功'/'删除申请记录成功'
     */
    @ApiOperation(value = "删除审核记录")//添加删除角色
    @DeleteMapping("/deleteRecord/{id}")
    public R<?> deleteRecord(@PathVariable("id") Long id){
        String data = projectService.deleteRecord(id);
        return R.success(data);
    }


    // =============================================== 附件 ===============================================
    @Value("${ip:localhost}") //没有就默认localhost
            String ip;
    @Value("${server.port}")
    String port;
    @Value("${file.root-path}")
    String rootPath;
    @Value("${file.temporary-path}")
    String temporaryPath;
    String basePath;
    private final String sep = File.separator;
    @PostConstruct
    public void init() {
        basePath = rootPath + sep + "project"; // files->project
    }


    //根据id上传附件,只有tid申请者可操作,直接执行上传附件

    /**
     * 根据id上传附件。id不存在/不是自己。会抛出自定义异常信息
     * @param id  关联的记录id
     * @param fileList 附件
     * @return
     * @throws IOException
     */
    @ApiOperation("[教师用户]上传附件")
    @PostMapping("/file/upload/{id}")
    public R<ArrayList<String>> upload(@PathVariable("id") Long id, MultipartFile[] fileList) throws IOException {
        //判断id是否存在
        ProjectAudit findRecord = this.checkIdExistAndReturn(id);
        Long tid = findRecord.getTid();
        if (!Objects.equals(tid, UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_UPLOAD);
        }
        // === 开始 ===
        String parentPath =  basePath + sep + tid + sep + id; // files->project->tid->id
        if (!FileUtil.exist(parentPath)) {
            FileUtil.mkdir(parentPath); // 创建文件根目录
        }
        ArrayList<String> urls = new ArrayList<>();
        for (MultipartFile file : fileList) {
            String fileName = file.getOriginalFilename(); // abc.png 文件全名
            String mainName = FileUtil.mainName(fileName); // abc 文件名
            String extName = FileUtil.extName(fileName);   // png 文件后缀
            // 如果当前上传的文件已经存在了，那么这个时候我就要重名一个文件名称
            int i = 1; // 采用windows文件重命名方法
            while (FileUtil.exist(parentPath + sep + fileName)) {
                fileName = mainName + "(" + i + ")" + "." + extName;
                i++;
            }
            File saveFile = new File(parentPath + sep + fileName);
            file.transferTo(saveFile); //将文件上传至对应磁盘位置
            String url = "http://" + ip + ":" + port + "/projectAudit/file/download/"  + id + "/" + fileName;
            urls.add(url);
        }
        return R.success(urls);
    }

    /**
     * 根据记录id,返回对应的附件列表,tid若不是教师用户/用户若没有对该uid的审核权限,抛出自定义异常
     * @param id 记录id
     */
    @ApiOperation("根据tid查询附件列表")
    @GetMapping("/file/list/{id}") //附件不会很多,暂不分页
    public R<List<FileInfo>> list(@PathVariable("id") Long id) {
        ProjectAudit findRecord = this.checkIdExistAndReturn(id);
        Long tid = findRecord.getTid();
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
        String parentPath = basePath + sep + tid + sep + id; // files->project->tid->id
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

    //根据id导出对应的文件夹
    /**
     * 根据记录id导出对应的文件夹,只有tid申请者可操作,直接执行导出
     * @param id 记录id
     * @throws IOException
     */
    @ApiOperation("根据记录id导出附件文件夹压缩包")
    @GetMapping("/file/export/{id}")
    public void export(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        //判断id是否存在
        ProjectAudit findRecord = this.checkIdExistAndReturn(id);
        Long tid = findRecord.getTid();
        //判断权限
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }
        // === 开始 ===
        //创建一个新的临时文件路径！！！
        if (FileUtil.exist(temporaryPath)) {
            FileUtil.del(temporaryPath);
        }
        //需要压缩的文件列表 fileList
        List<File> fileList = CollUtil.newArrayList();
        String parentPath =  basePath + sep + tid + sep + id; // files->project->tid->id
        if (!FileUtil.exist(parentPath)) {
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        fileList.add(new File(parentPath));
        //构造临时压缩文件 tempZipFile
        String teacherName = teacherMapper.getTeacherNameByTid(tid);
        String fileName = teacherName + "-项目附件.zip";
        File tempZipFile = new File(temporaryPath + sep + fileName);
        ZipUtil.zip(tempZipFile, true, fileList.toArray(new File[fileList.size()]));
        MyUtil.downloadZip(tempZipFile, response);
    }

    /**
     * 下载附件 无拦截。 id/文件不存在,则返回对应信息
     * @param id  记录id.
     * @param fileName 项目附件文件名,全称(带后缀)
     * @param response
     * @throws IOException
     */
    @AuthAccess
    @ApiOperation("下载项目附件")
    @GetMapping("/file/download/{id}/{fileName}")
    public void download(@PathVariable("id")Long id,
                         @PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        ProjectAudit findRecord = this.checkIdExistAndReturn(id);
        Long tid = findRecord.getTid();
        String parentPath =  basePath + sep + tid + sep + id; // files->project->tid->id
        String filePath = parentPath + sep + fileName; // files->project->tid->id->fileName
        if (!FileUtil.exist(filePath)) {
            log.error("文件不存在：{}", filePath);
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        //对文件名编码,解决中文文件名乱码问题
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 下载
        //response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 预览
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 是否有该文件存在,有则说明已有附件。用于检查文件名是否存在。(用户重命名的时候,如果重名继续会覆盖原文件)
     * @param id 记录id
     * @param fileName 文件名,全称(带后缀)
     * @return 返回true/false
     */
    @ApiOperation("[教师用户]检查自己的该文件是否存在")
    @GetMapping("/file/isExist/{id}/{fileName}")
    public R<?> isExist(@PathVariable("id") Long id, @PathVariable("fileName") String fileName) {
        MyUtil.checkIsTeacher();//判断是否是教师
        Long uid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + uid + sep + id; // files->project->uid->id
        String filePath = parentPath + sep + fileName;
        if (!FileUtil.exist(filePath)) {//如果不存在
            return R.success(false);
        }
        return R.success(true);
    }

    /**
     * 重命名文件,如果重名执行覆盖,请先调用检查。检验oldFileName,文件不存在,抛出自定义异常信息。
     * @param oldFileName 原文件名,全称(带后缀)
     * @param newMainName 新名称: !!!不带后缀!!!
     */
    @ApiOperation("[教师用户]重命名文件")
    @PutMapping("/file/rename/{id}")
    public R<?> rename(@PathVariable("id") Long id, @RequestParam String oldFileName,@RequestParam String newMainName)
    {
        MyUtil.checkIsTeacher();//判断是否是教师
        Long uid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + uid + sep + id; // files->project->uid->id
        String filePath = parentPath + sep + oldFileName;
        if (!FileUtil.exist(filePath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.rename(new File(filePath),newMainName,true,true);//重命名文件如果存在则覆盖
        return R.success("重命名成功");
    }

    /**
     * 删除的项目文件夹(包含里面所有项目文件)。且自己才能删除自己的附件。若不存在则返回文件夹不存在
     */
    @ApiOperation("[教师用户]删除自己的所有项目文件夹")
    @DeleteMapping("/file/deleteAll")
    public R<?> delete(){
        MyUtil.checkIsTeacher();//判断是否是教师
        Long tid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + tid;
        if (!FileUtil.exist(parentPath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.del(parentPath);//删除文件夹以及文件夹下的文件
        return R.success("删除原附件成功");
    }

    /**
     * 根据记录id删除自己的附件文件夹。且自己才能删除自己的附件。若不存在则返回文件夹不存在
     */
    @ApiOperation("[教师用户]根据记录id删除自己的对应文件夹")
    @DeleteMapping("/file/deleteAll/{id}")
    public R<?> delete(@PathVariable("id") Long id){
        MyUtil.checkIsTeacher();//判断是否是教师
        Long tid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + tid + sep + id; // files->project->tid->id
        if (!FileUtil.exist(parentPath)) {//如果不存在
            throw new CustomException(CustomExceptionCodeMsg.FILE_NOT_EXIST);
        }
        FileUtil.del(parentPath);//删除文件夹以及文件夹下的文件
        return R.success("删除原附件成功");
    }

    /**
     * 根据id和具体文件名删除自己的项目附件。如果是最后一个文件,则删除父级文件夹。检验fileName抛出自定义异常。
     * @param id 记录id
     * @param fileName 文件名,全称(带后缀)
     */
    @ApiOperation("[教师用户]删除自己的项目附件")
    @DeleteMapping("/file/delete/{id}/{fileName}")
    public R<?> delete(@PathVariable("id") Long id,@PathVariable("fileName") String fileName){
        MyUtil.checkIsTeacher();//判断是否是教师
        Long uid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + uid + sep + id; // files->project->uid->id
        String filePath = parentPath + sep + fileName; // files->project->uid->id->fileName
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
     * 是否有父级文件夹,有则说明已有附件
     * return true/false
     */
    @ApiOperation("[教师用户]判断自己是否已有项目附件")
    @GetMapping("/file/isExist")
    public R<?> isExist() {
        MyUtil.checkIsTeacher();//判断是否是教师
        Long uid = UserInfoContext.getUser().getId();
        String parentPath =  basePath + sep + uid; // files->project->uid
        if (!FileUtil.exist(parentPath)) {//如果不存在
            return R.success(false);
        }
        return R.success(true);
    }

    private ProjectAudit checkIdExistAndReturn(Long id) {
        ProjectAudit findRecord = projectService.getById(id);
        if (findRecord == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        return findRecord;
    }

}
