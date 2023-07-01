/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tygy.bishe.tool.BatchSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置MybatisPlus的分页插件
 * @author qinyuxiang
 * @version 1.0
 * @date 2022/12/27
 */
@Configuration
public class MybatisPlusConfig {

    /**
     *  分页拦截器
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

    /**
     * 批量插入
     *
     * @return
     */
    @Bean
    public BatchSqlInjector easySqlInjector() {
        return new BatchSqlInjector();
    }

}
