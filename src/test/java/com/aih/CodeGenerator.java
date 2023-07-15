package com.aih;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/teacher_info_manage_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";
        String author = "AiH";
        String outputDir = "D:\\idea\\2023\\TeacherInfoManage\\src\\main\\java";
        String basePackage = "com";
        String moduleName = "aih";
        String mapperLocation = "D:\\idea\\2023\\TeacherInfoManage\\src\\main\\resources\\mapper\\" + moduleName;
//        String tableName = "academic_paper_audit,admin,college,education_experience_audit,honorary_award_audit," +
//                "identity_card_audit,office,project_audit,software_audit,super_admin,teacher,topic_audit,work_experience_audit";
        String tableName = "role,teacher_role";
        String tablePrefix = "";  //表的前缀
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件 建议不要覆盖
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(basePackage) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix(tablePrefix); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
