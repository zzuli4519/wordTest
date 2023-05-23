package com.example.wordtest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * created by: FoAng
 * create time: 19/5/2023 1:26 下午
 */
@RestController
@RequestMapping("/word")
@Api(description = "解析图片", tags = "测试word转换")
public class Controller {

    @PostMapping("/previewWord")
    @ApiOperation("预览word图片")
    public Result<DocImageVo> previewWord(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestPart("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            DocImageVo docImageVo = WordUtil.parseWordImageByPage(inputStream, page, 150);
            return Result.ok(docImageVo);
        } catch (Exception e) {
            return Result.fail("获取预览图片失败");
        }
    }

}
