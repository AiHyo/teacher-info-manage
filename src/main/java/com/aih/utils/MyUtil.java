package com.aih.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.entity.Teacher;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.TeacherMapper;
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

    /**
     * 获取有权利查看tid审核信息的上级ids,**包括tid自己
     */
    public List<Long> getPowerIdsByTid(Long tid){
        log.info("获取有权限操作{}的id",tid);
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
            powerIds = teacherMapper.getAuditorIdsByOid(oid);
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

    public static void downloadZip(File file, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        byte[] bytes = FileUtil.readBytes(file);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        //删除该临时zip包(此zip包任何时候都不需要保留,因为源文件随时可以再次进行压缩生成zip包)
//        FileUtil.del(file);
    }
}
