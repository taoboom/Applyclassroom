/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.config;


import com.tygy.bishe.common.JacksonObjectMapper;
import com.tygy.bishe.tool.LoginInterceptor;
import com.tygy.bishe.tool.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2022/9/25
 */

/**
 * @Configuration 当前类是配置类  把当前的对象注入到spring容器里
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径  拦截所有的请求
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("*")
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // registry注册器  order执行顺序  小的先执行
        //登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/pic/**",
                        "/user/login",
                        "/role/getRoleData"
                ).order(1);
        //token刷新器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);

    }




    /**
     * 扩展mvc框架的消息转换器(解决雪花算法生成的id传到前端精度缺失问题，通过将long类型转换为string)
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        final MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }


    /**
     * 实现前端Vue :scr 根据url显示头像图片，则必须设置WebMVC中的静态资源配置
     * addResourceHandler：访问映射路径
     * addResourceLocations：资源绝对路径
     */

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**").addResourceLocations("file:"+fileUploadPath);
    }

}
