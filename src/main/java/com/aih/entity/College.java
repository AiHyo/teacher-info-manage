package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 学院
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */

@Data
@ApiModel(value = "College对象", description = "学院")
public class College implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学院id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("学院名称")
    @TableField("zw_college_name")
    private String collegeName;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

    @ApiModelProperty("备用1")
    private String test1;

    @ApiModelProperty("备用2")
    private String test2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }
    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    @Override
    public String toString() {
        return "College{" +
            "id=" + id +
            ", collegeName=" + collegeName +
            ", test1=" + test1 +
            ", test2=" + test2 +
        "}";
    }
}
