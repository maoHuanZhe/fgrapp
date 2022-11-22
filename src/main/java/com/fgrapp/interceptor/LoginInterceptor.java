package com.fgrapp.interceptor;

import com.fgrapp.domain.SysUserDo;
import com.fgrapp.result.ResultStatus;
import com.fgrapp.util.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fgr
 * @date 2022-11-05 10:25
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取User
        SysUserDo user = UserHolder.getUser();
        //判断用户是否存在
        if (user == null) {
            //如果不存在 拦截请求，设置状态码为401
            response.setStatus(ResultStatus.NOT_LOGIN.getCode());
            return false;
        }
        //放行
        return true;
    }
}
