package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "文件上传的相关接口")
@Slf4j
public class CommonConfiguration {
    @Autowired
    private  AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation(value = "进行文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("开始进行文件上传:{}", file);
        try {
            //获取原始的文件名
            String originalFilename = file.getOriginalFilename();

            //获取文件名的后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //使用uuid
            String fileName = UUID.randomUUID().toString() +"."+suffix;

            //获取文件的请求路径
            String filepath = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(filepath);
        } catch (IOException e) {
            log.info("文件上传失败:{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
