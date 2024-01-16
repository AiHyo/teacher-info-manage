package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aih.entity.*;
import com.aih.entity.dto.TeacherDto;
import com.aih.entity.vo.*;
import com.aih.mapper.*;
import com.aih.service.*;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 管理员 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TeacherRoleMapper teacherRoleMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private ICollegeService collegeService;
    @Autowired
    private IOfficeService officeService;
    @Autowired
    private RequestCollegeChangeMapper requestCollegeChangeMapper;
    @Autowired
    private MyUtil myUtil;
    @Resource //@Autowired
    private HttpServletResponse response;

    private final String sep = File.separator;
    @Value("${file.root-path}")
    String rootPath;
    @Value("${file.temporary-path}")
    String temporaryPath;
    @Value("${excel.file-name}")
    String defaultExcelName;
    @Value("${default-password}")
    String defaultPassword;


    /**
     * 接收username和password。登录成功返回token
     */
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Admin admin) {
        Map<String, Object> data = adminService.login(admin);
        return R.success(data);
    }

    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public R<?> logout() {
        adminService.logout();
        return R.success("登出成功");
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/showInfo")
    public R<AdminVo> showInfo() {
        Admin admin = adminService.showInfo();
        return R.success(new AdminVo(admin, collegeService.getCollegeNameByCid(admin.getCid())));
    }


    /**
     * 修改自己的密码
     * @param oldPassword 原本密码
     * @param newPassword 新密码
     */
    @ApiOperation("修改密码")
    @PutMapping("/updatePassword")
    public R<?> updatePassword(@RequestParam("oldPassword") String oldPassword,
                               @RequestParam("newPassword") String newPassword) {
        Long uid = UserInfoContext.getUser().getId();
        Admin admin = adminService.getById(uid);
        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) { // 旧密码不正确
            throw new CustomException(CustomExceptionCodeMsg.OLD_PASSWORD_ERROR);
        }
        //修改密码
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminService.updateById(admin);
        return R.success("修改密码成功");
    }


    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return R.success("修改成功");
    }

    /**
     * 修改教师信息,如果[tid/oid]不存在/不属于自己学院,抛出自定义异常信息
     * username,cid强制置空,不允许修改.password自动加密
     * @param teacherDto
     * @return
     */
    @ApiOperation("修改教师信息")
    @PutMapping("/updateTeacher")
    public R<?> updateTeacher(@RequestBody TeacherDto teacherDto){
        //检验tid
        Long tid = teacherDto.getId();
        this.checkTeacherIds(CollUtil.newArrayList(tid));
        if (StrUtil.isNotBlank(teacherDto.getPassword())) {
            teacherDto.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
        }
        if (StrUtil.isNotBlank(teacherDto.getUsername())) {
            teacherDto.setUsername(null);
        }
        if (teacherDto.getCid()!=null) {
            teacherDto.setCid(null);
        }
        //如果有oid,检验oid
        Long oid = teacherDto.getOid();
        if (oid !=null) {
            Office findOffice = officeMapper.selectById(oid);
            if (findOffice == null) {
                throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_OFFICE);
            }
            if (oid!=0 && !findOffice.getCid().equals(UserInfoContext.getUser().getCid())) {
                throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
            }
        }
        MyUtil.checkPhone(teacherDto.getPhone());//检验手机号格式
        List<Long> roleIds = teacherDto.getRids();
        if (!roleIds.isEmpty()){
            roleService.updateTeacherRole(tid,roleIds);
        }
        Teacher teacher = new Teacher();
        teacher.setId(tid);
        teacher.setTeacherName(teacherDto.getTeacherName());
        teacher.setUsername(teacherDto.getUsername());
        teacher.setPassword(teacherDto.getPassword());
        teacher.setGender(teacherDto.getGender());
        teacher.setEthnic(teacherDto.getEthnic());
        teacher.setNativePlace(teacherDto.getNativePlace());
        teacher.setAddress(teacherDto.getAddress());
        teacher.setPhone(teacherDto.getPhone());
        teacher.setPoliticsStatus(teacherDto.getPoliticsStatus());
        teacher.setEducationDegree(teacherDto.getEducationDegree());
        teacher.setIdNumber(teacherDto.getIdNumber());
        teacher.setStartDate(teacherDto.getStartDate());
        teacher.setCreateDate(teacherDto.getCreateDate());
        teacher.setDeleted(teacherDto.getDeleted());
        teacher.setOid(teacherDto.getOid());
        teacherService.updateById(teacher);
        return R.success("修改成功");
    }

    /**
     * isAuditor/ids非法 抛出自定义信息。审核员权限改动了的教师,权限生效时间createDate会被更新成当前时间
     * @param ids 教师id列表
     * @param isAuditor 路径参数 0:设为非审核员  1:设为审核员
     */
    @ApiOperation("(批量)修改审核员权限")
    @PostMapping("/auditorPower/{isAuditor}")
    public R<String> auditorPower(@PathVariable Integer isAuditor, @RequestParam List<Long> ids) {
        //判断ids是否合法
        this.checkTeacherIds(ids);
        //判断isAuditor是否合法
        if (isAuditor != 0 && isAuditor != 1) {
            throw new CustomException(CustomExceptionCodeMsg.ISAUDITOR_ILLEGAL);
        }
        adminService.updateIsAuditor(isAuditor, ids);
        return R.success("更改审核员权限成功");
    }

    @ApiOperation("(批量)重置密码")
    @PutMapping("/resetPassword")
    public R<String> resetPassword(@RequestParam List<Long> ids,@RequestParam(required = false) String password) {
        if (StrUtil.isBlank(password)) {
            password = defaultPassword;
        }
        //判断ids是否合法
        this.checkTeacherIds(ids);
        adminService.resetPassword(ids,password);
        return R.success("重置密码成功");
    }

/*
    @ApiOperation("查询学院下的教研室")
    @GetMapping("/getOfficeList")
    public R<List<Office>> getOfficeList(@RequestParam(value = "officeName", required = false) String officeName) {
        Long cid = UserInfoContext.getUser().getCid();
        List<Office> officeList = officeMapper.getOfficeList(cid, officeName);
        return R.success(officeList);
    }*/

//    /**
//     * 可选模糊查询 keyword: 教研室/教师名称,其它都是对应教师属性的模糊查询
//     */
//    @ApiOperation("查询学院下的教师")
//    @GetMapping("/getTeacherList")
//    public R<Page<TeacherDto>> admin_GetTeacherList(@RequestParam("pageNum") Integer pageNum,
//                                                    @RequestParam("pageSize") Integer pageSize,
//                                                    @RequestParam(value = "keyword", required = false) String keyword,
//                                                    @RequestParam(value = "oid", required = false) Long oid,
//                                                    @RequestParam(value = "officeName", required = false) String officeName,
//                                                    @RequestParam(value = "teacherName", required = false) String teacherName,
//                                                    @RequestParam(value = "isAuditor", required = false) Integer isAuditor,
//                                                    @RequestParam(value = "gender", required = false) Integer gender,
//                                                    @RequestParam(value = "ethnic", required = false) String ethnic,
//                                                    @RequestParam(value = "birthplace", required = false) String birthplace,
//                                                    @RequestParam(value = "address", required = false) String address) {
//        return R.success(adminService.getTeacherList(pageNum,pageSize, keyword, oid,officeName,teacherName,isAuditor, gender, ethnic, birthplace, address));
//    }


//    @ApiOperation("查看教师详细信息")
//    @GetMapping("/getTeacherInfo/{tid}")
//    public R<TeacherDetailDto> getTeacherInfo(@PathVariable Long tid) {
//        //判断权限
//        Teacher findTeacher = teacherService.getById(tid);
//        if (findTeacher == null) {
//            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
//        }
//        if (!findTeacher.getCid().equals(UserInfoContext.getUser().getCid())) {
//            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
//        }
//        return R.success(teacherService.queryTeacherDtoByTid(tid));
//    }

//    /**
//     * 查询的是所有类型的信息,统一部分内容检验auditStatus是否合法,不合法抛出自定义信息
//     * @param pageNum  当前页码
//     * @param pageSize 每页大小
//     * @param auditStatus (可选)审核状态,只接受0/1/2,不传则查询所有
//     * @param onlyOwn 布尔类型(可选)只看自己的,默认false
//     * @return 每条数据包含：审核类型、审核对象id、审核状态、创建时间、审核时间、教师姓名、教研室名称、学院名称
//     */
//    @ApiOperation("[预览]查询管理学院下所有的审核员审核记录")
//    @GetMapping("/getAuditList")
//    public R<Page<AuditInfoDto>> getAuditList(@RequestParam("pageNum") Integer pageNum,
//                                              @RequestParam("pageSize") Integer pageSize,
//                                              @RequestParam(value = "auditStatus", required = false) Integer auditStatus,
//                                              @RequestParam(value = "onlyOwn",required = false,defaultValue = "false")boolean onlyOwn) {
//        if(auditStatus!=null){
//            MyUtil.checkAuditStatus(auditStatus);//检查auditStatus参数是否合法
//        }
//        return R.success(adminService.getAuditList(pageNum,pageSize, auditStatus,onlyOwn));
//    }

    //根据ids删除教师
/**
     * 根据ids删除教师,检验ids是否合法,不合法抛出自定义信息
     * @param ids 教师id列表
     */
    @ApiOperation("删除教师")
    @DeleteMapping("/deleteTeacher")
    public R<?> deleteTeacher(@RequestParam List<Long> ids) {
        //判断ids是否合法
        this.checkTeacherIds(ids);
        teacherService.removeByIds(ids);
        return R.success("删除教师成功");
    }

    // ============================= role =============================
//    @ApiOperation("查看所有职务")
//    @GetMapping("/showRoleList")
//    public R<List<Role>> getAllRole() {
//        return R.success(roleMapper.selectList(null));
//    }

    /**
     * @param roleName 新职务名
     */
    @ApiOperation("增加职务")
    @PostMapping("/addRole")
    public R<?> addRole(@RequestParam String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        roleMapper.insert(role);
        return R.success("增加职务成功");
    }


    /**
     * @param id      职务id
     */
    @ApiOperation("删除职务")
    @DeleteMapping("/deleteRole/{id}")
    public R<?> deleteRole(@PathVariable Long id) {
        roleMapper.deleteById(id);
        return R.success("删除职务成功");
    }
    // ============================= 办公室 =============================
    @ApiOperation("查看自己学院下的办公室")
    @GetMapping("/getAllOffice")
    public R<Page<OfficeVo>> getAllOfficeDto(@RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize,
                                             @RequestParam(value = "officeName", required = false) String officeName){
        return R.success(officeService.getOfficeByCollege(pageNum,pageSize,officeName));
    }
    /**
     * 在当前学院添加办公室 如果办公室名已存在,则返回自定义信息
     * @param officeName 新办公室名
     */
    @ApiOperation("添加办公室")
    @PostMapping("/addOffice")
    public R<?> addOffice(@RequestParam String officeName){
        Long oid = officeService.getOidByName(officeName);
        if (oid != null) {
            throw new CustomException(CustomExceptionCodeMsg.OFFICE_NAME_EXIST);
        }
        Office office = new Office();
        office.setOfficeName(officeName);
        office.setCid(UserInfoContext.getUser().getCid());
        officeService.save(office);
        return R.success("添加成功");
    }

    /**
     * 根据id修改办公室名称。id不存在/不是自己学院的,不存在抛出自定义信息。办公室新名称已存在则返回自定义信息
     * @param id    办公室id
     * @param officeName 办公室新名称
     * @return
     */
    @ApiOperation("修改办公室名称")
    @PutMapping("/updateOfficeName")
    public R<?> updateOfficeName(@RequestParam("id") Long id,
                                 @RequestParam("officeName") String officeName){
        Office findOffice = officeMapper.selectById(id);
        if (findOffice == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        if (!findOffice.getCid().equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        if (officeService.getOidByName(officeName) != null) {
            throw new CustomException(CustomExceptionCodeMsg.OFFICE_NAME_EXIST);
        }
        findOffice.setOfficeName(officeName);
        officeService.updateById(findOffice);
        return R.success("修改成功");
    }

    /**
     * 根据id删除办公室,id不存在/不是自己学院的,抛出自定义异常信息
     * @param id 办公室的id
     */
    @ApiOperation("删除办公室")
    @DeleteMapping("/deleteOffice/{id}")
    public R<?> deleteOffice(@PathVariable Long id){
        //判断id是否存在
        Office findOffice = officeMapper.selectById(id);
        if (findOffice == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        //判断是否是自己学院的办公室
        if (!findOffice.getCid().equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        //判断没有教师是该办公室的才可以删除
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getOid,id);
        long count = teacherService.count(queryWrapper);
        if (count!=0) {
            throw new CustomException(CustomExceptionCodeMsg.OFFICE_DELETE_ERROR);
        }
        officeMapper.deleteById(id);
        return R.success("删除成功");
    }
    // =============================== 修改教师办公室 ===============================
    /**
     * 根据tid修改教师的办公室。检验tid是否存在/有无权限,oid是否存在/是自己学院下
     * @param tid 教师id
     * @param oid 办公室id
     */
    @ApiOperation("修改教师的办公室")
    @PutMapping("/updateTeacherOffice/{tid}/{oid}")
    public R<?> updateTeacherOffice(@PathVariable Long tid,@PathVariable Long oid){
        //判断tid是否存在/有无权限
        this.checkTeacherIds(CollUtil.newArrayList(tid));
        //判断oid是否存在/有无权限
        Office findOffice = officeMapper.selectById(oid);
        if (findOffice == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_OFFICE);
        }
        if (!findOffice.getCid().equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        // 开始操作
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Teacher::getId,tid);
        updateWrapper.set(Teacher::getOid,oid);
        teacherService.update(null,updateWrapper);
        return R.success("修改成功");
    }
    // ================================ 申请教师换学院 ================================
    //申请tid换学院到新的cid
    /**
     * 自动填充oldAid+oldCid+createTime+auditStatus。如果(tid不存在/不属于自己学院)/(newCid不存在/是自身学院)=>非法操作,抛出自定义异常信息。
     * @param tid 需要申请的教师id
     * @param newCid 新学院id
     * @param oldAdminRemark 旧管理员(申请者)备注
     */
    @ApiOperation("申请教师换学院")
    @PostMapping("/applyChangeTeacherCollege/{tid}/{newCid}")
    public R<?> applyChangeTeacherCollege(@PathVariable Long tid,@PathVariable Long newCid,
                                          @RequestParam(value = "oldAdminRemark",required = false) String oldAdminRemark) {
        Teacher findTeacher = myUtil.checkTidExistAndReturnTeacher(tid);//判断tid是否存在
        myUtil.checkCidExistAndReturnCollege(newCid);//判断cid是否存在
        //tid是否是当前学院
        if (!findTeacher.getCid().equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.REQUEST_CHANGE_COLLEGE_ERROR_TID_IS_SELF);
        }
        //判断cid是否是当前学院
        Long oldCid = UserInfoContext.getUser().getCid();
        if (newCid.equals(oldCid)) {
            throw new CustomException(CustomExceptionCodeMsg.REQUEST_CHANGE_COLLEGE_ERROR_TID_IS_SELF);
        }
        //判断tid是否正在申请换学院
        Integer unAuditCountByTid = requestCollegeChangeMapper.getUnAuditCountByTid(tid);
        if (unAuditCountByTid != 0) {
            throw new CustomException(CustomExceptionCodeMsg.REQUEST_CHANGE_COLLEGE_ERROR_TID_IS_AUDITING);
        }
        adminService.applyChangeTeacherCollege(tid, newCid, oldAdminRemark);
        return R.success("申请成功");
    }

    /**
     * 检验auditStatus是否合法,不合法抛出自定义信息
     * @param pageNum
     * @param pageSize
     * @param auditStatus (可选)审核状态,只接受0/1/2,不传则查询所有
     * @return
     */
    @ApiOperation("查看学院转出的申请")
    @GetMapping("/getChangeCollegeRequestList")
    public R<Page<RequestCollegeChangeVo>> getChangeCollegeRequestList(@RequestParam("pageNum") Integer pageNum,
                                                                       @RequestParam("pageSize") Integer pageSize,
                                                                       @RequestParam(value = "auditStatus", required = false) Integer auditStatus,
                                                                       @RequestParam(value = "onlyOwn",required = false,defaultValue = "false")boolean onlyOwn) {
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);//检查auditStatus参数是否合法
        }
        return R.success(adminService.getChangeCollegeRequestList(pageNum,pageSize, auditStatus,onlyOwn));
    }

    /** 检验auditStatus是否合法,不合法抛出自定义信息
     * @param pageNum
     * @param pageSize
     * @param auditStatus (可选)审核状态,只接受0/1/2,不传则查询所有
     * @param onlyOwn 布尔类型(可选)只看自己的,默认false
     * @return
     */
    @ApiOperation("查看转来学院的申请")
    @GetMapping("/getChangeCollegeAuditList")
    public R<Page<RequestCollegeChangeVo>> getChangeCollegeAuditList(@RequestParam("pageNum") Integer pageNum,
                                                                     @RequestParam("pageSize") Integer pageSize,
                                                                     @RequestParam(value = "auditStatus", required = false) Integer auditStatus,
                                                                     @RequestParam(value = "onlyOwn",required = false,defaultValue = "false")boolean onlyOwn) {
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);//检查auditStatus参数是否合法
        }
        return R.success(adminService.getChangeCollegeAuditList(pageNum,pageSize, auditStatus,onlyOwn));
    }

    /**
     * 更改学院成功后教师的oid会自动设为0(officeName是暂无)。自动填充auditTime+auditStatus+newAid+newCid。id不存在/不是当前学院的申请/不是待审核状态=>非法操作,抛出自定义异常信息。
     * @param id
     * @param newAdminRemark
     * @return
     */
    @ApiOperation("通过换学院申请")
    @PutMapping("/passChangeCollege/{id}")
    public R<?> passChangeCollege(@PathVariable Long id,
                                  @RequestParam(value = "newAdminRemark",required = false) String newAdminRemark) {
        RequestCollegeChange requestCollegeChange = requestCollegeChangeMapper.selectById(id);
        if (requestCollegeChange == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_REQUEST_COLLEGE_CHANGE);
        }
        Long newCid = requestCollegeChange.getNewCid();
        //判断是否是当前学院的申请
        if (!newCid.equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_AUDIT);
        }
        //判断是否是待审核状态
        if (requestCollegeChange.getAuditStatus() != 0) {
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_ERROR_NOT_UNAUDIT);
        }
        //通过申请
        adminService.passChangeCollege(requestCollegeChange, newAdminRemark);
        return R.success("通过申请成功");
    }


    @ApiOperation("拒绝换学院申请")
    @PutMapping("/refuseChangeCollege/{id}")
    public R<?> refuseChangeCollege(@PathVariable Long id,
                                    @RequestParam(value = "newAdminRemark",required = false) String newAdminRemark) {
        RequestCollegeChange requestCollegeChange = requestCollegeChangeMapper.selectById(id);
        if (requestCollegeChange == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_REQUEST_COLLEGE_CHANGE);
        }
        Long newCid = requestCollegeChange.getNewCid();
        //判断是否是当前学院的申请
        if (!newCid.equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_AUDIT);
        }
        //判断是否是待审核状态
        if (requestCollegeChange.getAuditStatus() != 0) {
            throw new CustomException(CustomExceptionCodeMsg.AUDIT_ERROR_NOT_UNAUDIT);
        }
        //拒绝申请
        adminService.refuseChangeCollege(requestCollegeChange, newAdminRemark);
        return R.success("拒绝申请成功");
    }

    //如果是oldAid本人并且未审核,则直接删除当前记录,如果是其它则添加删除角色
    /**
     * 根据id查找记录,若未审核,只有oldAid申请者可操作,直接执行逻辑删除(删除记录全部人都看不见)。
     * 若已审核,oldAid/newAid(申请者/审核者)都可操作,但都只是删除自己的记录(删除自己的显示列表)。
     * 检验 id不存在/没有权限/已删除过 会返回自定义信息
     * @param id 换学院申请记录id
     * @return 根据情况返回'删除审核申请成功'/'删除审核记录成功'
     */
    @ApiOperation("删除换学院申请记录")
    @DeleteMapping("/deleteChangeCollege/{id}")
    public R<?> deleteChangeCollege(@PathVariable Long id) {
        String data = adminService.deleteChangeCollege(id);
        return R.success(data);
    }


//    //==========================================excel======================================
//    /**
//     * @param ids       （可选）
//     * @param oids      （可选）教研室oids
//     * @param fileName  （可选）导出excel的名称,默认"教师信息表"
//     * @param fieldList 只导出部分字段（可选）id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
//     * @throws IOException
//     */
//    @LogAnnotation(module = "管理员", operator = "Excel批量导出教师信息")
//    @ApiOperation("Excel批量导出教师信息")
//    @GetMapping("/export")
//    public void exportExcel(@RequestParam(value = "ids", required = false) List<Long> ids,
//                            @RequestParam(value = "oids", required = false) List<Long> oids,
//                            @RequestParam(value = "fileName", required = false) String fileName,
//                            @RequestParam(value = "fieldList", required = false) List<String> fieldList) throws IOException {
//        if (StrUtil.isBlank(fileName)) {
//            fileName = defaultExcelName;
//        }
//        List<Teacher> teacherList = this.admin_GetTeacherList(ids, oids);
//        // ===ExcelWriter:excel写入器===
//        ExcelWriter writer = teacherService.getMyExcelWriter(teacherList, fileName, fieldList);
//        //在浏览器下载：设置response并写出xlsx
//        fileName = URLEncoder.encode(fileName, "UTF-8");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//        ServletOutputStream outputStream = response.getOutputStream();
//        writer.flush(outputStream, true);//true 关闭输出流
//        writer.close();
//        outputStream.flush();
//        outputStream.close();
//    }
//
//    //==========================================word======================================
//    @ApiOperation("导出Word")
//    @GetMapping("/exportWord/{tid}")
//    public void exportWord(@PathVariable Long tid) throws IOException {
//        //判断权限
//        Teacher findTeacher = teacherService.getById(tid);
//        if (findTeacher == null) {
//            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
//        }
//        if (!findTeacher.getCid().equals(UserInfoContext.getUser().getCid())) {
//            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
//        }
//        //获取XWPFTemplate word写入器 渲染好数据的word
//        XWPFTemplate render = teacherService.getWordRender(findTeacher);
//        //在浏览器下载：设置response并写出docx
//        String fileName = URLEncoder.encode(findTeacher.getTeacherName() + "教师信息表", "UTF-8");
//        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".docx");
//        ServletOutputStream outputStream = response.getOutputStream();
//        render.writeAndClose(outputStream);
//        render.close();//双重保障
//        outputStream.flush();
//        outputStream.close();
//    }
//
//
//    //============================================压缩包================================================
//
//    @ApiOperation("导出压缩包")
//    @GetMapping("/exportZip/")
//    @LogAnnotation(module="管理员",operator="导出压缩包")
//    public void exportZip(@RequestParam(value = "ids", required = false) List<Long> ids,
//                          @RequestParam(value = "oids", required = false) List<Long> oids,
//                          @RequestParam(value = "fieldList", required = false) List<String> fieldList,
//                          @RequestParam(value = "attachmentList", required = false) List<String> attachmentList) throws IOException {
//        //创建一个新的临时文件路径！！！
//        if (FileUtil.exist(temporaryPath)) {
//            FileUtil.del(temporaryPath);
//        }//创建临时zip包
//        File tempZipFile = new File(temporaryPath + sep + "test压缩.zip");
//        //需要压缩的文件列表
//        List<File> fileList = CollUtil.newArrayList();
//        //调用封装函数获取教师列表
//        List<Teacher> teacherList = this.admin_GetTeacherList(ids, oids);
//        //添加需要压缩的文件
//        fileList.add(teacherService.getTeacherAttachmentFolder(teacherList,attachmentList));//教师附件文件夹
//        fileList.add(teacherService.getMyExcelFile(teacherList, fieldList)); //excel
//        fileList.add(teacherService.getMyWordFolder(teacherList));           //word
//        fileList.add(new File(rootPath + sep + "qwq.jpg"));
//        //开始压缩
//        ZipUtil.zip(tempZipFile, true, fileList.toArray(new File[fileList.size()]));
//        //下载zip到浏览器！！！
//        MyUtil.downloadZip(tempZipFile, response);
//        //统一删除所有临时文件！！！！！！
//        FileUtil.del(temporaryPath);
//    }



    //==========================================封装方法=============================================
    //获取需要的teacherList
    private List<Teacher> admin_GetTeacherList(List<Long> ids, List<Long> oids) {
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
        } else if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
                }
                if (!teacher.getCid().equals(u_cid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法id就根据ids选
            queryWrapper.in(Teacher::getId, ids);
        } else log.info("没有选择指定教师,默认选择管理学院下所有教师");
        return teacherService.list(queryWrapper);
    }

    private void checkTeacherIds(List<Long> ids) {
        //判断ids是否全部都存在
        LambdaQueryWrapper<Teacher> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.in(Teacher::getId, ids);
        long count = teacherService.count(queryWrapper_1);
        if (count != ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        //判断ids是否都是当下学院的(权限
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Teacher::getId, ids);
        List<Teacher> teachers = teacherService.list(queryWrapper);
        for (Teacher teacher : teachers) {
            if (!teacher.getCid().equals(cid)) {
                throw new CustomException(CustomExceptionCodeMsg.UPDATE_AUDIT_POWER_ERROR);
            }
        }
    }






}
