package com.example.wordtest;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastJsonAutoConfig {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {
        //创建FastJson消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //创建FastJson配置对象
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue, //保留空的字段
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero,//Number null -> 0
                SerializerFeature.WriteNullBooleanAsFalse,//Boolean null -> false
                SerializerFeature.DisableCircularReferenceDetect //禁用循环引用检测
        );
        //将配置添加到转换器中
        converter.setFastJsonConfig(config);
        return converter;
    }
}
