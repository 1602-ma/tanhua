package com.feng.server.interceptor;

import com.aliyuncs.utils.StringUtils;
import com.feng.domain.po.User;
import com.feng.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author f
 * @date 2023/5/2 21:49
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=================================，进入前置拦截器");
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            User user = userService.getUserByToken(token);
            if (null == user) {
                response.setStatus(401);
                return false;
            }
            UserHolder.setUser(user);
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
