package com.example.wordtest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * created by: FoAng
 * create time: 16/5/2023 11:48 上午
 */
@Data
@ApiModel("word图片解析")
public class DocImageVo implements Serializable {

    @ApiModelProperty("总页数")
    private int totalCount;

    @ApiModelProperty("当前页数")
    private int current;

    @ApiModelProperty("图片内容,base64解析格式")
    private String content;

}
