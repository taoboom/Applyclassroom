/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.tool;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2022/11/5
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //这里一定不要忘了设置OPTIONS这个跨域请求，第一个请求一般不带token，然后会发起第二次请求带token
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

       //1。判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            //没有，需要拦截设置状态码
            response.setStatus(401);
            //拦截
            return false;
        }
        //有用户，则放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
