/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.tool;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tygy.bishe.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2022/11/5
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //这里一定不要忘了设置OPTIONS这个跨域请求，第一个请求一般不带token，然后会发起第二次请求带token
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        //获取请求头中的token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            return true;
        }


        //基于token获取redis中的用户
        String user = stringRedisTemplate.opsForValue()
                .get(RedisConstants.LOGIN_USER_KEY + token);
        // 判断用户是否存在
        if (user.isEmpty()) {
            return true;
        }

        //将json转为对象
        final UserDto userDto = JSONUtil.toBean(user, UserDto.class,false);

        // 存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDto);

        // 刷新token有效期
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
