package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AliyunConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliyun(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传的对象:{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
