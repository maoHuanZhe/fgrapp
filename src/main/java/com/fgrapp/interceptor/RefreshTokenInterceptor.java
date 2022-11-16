package com.fgrapp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.fgrapp.domain.SysUserDo;
import com.fgrapp.util.RedisConstants;
import com.fgrapp.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fgr
 * @date 2022-11-05 10:25
 **/
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("authorization");
        if (token == null) {
            return true;
        }
        //获取User
        Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + token);
        //判断用户是否存在
        if (objectMap.isEmpty()) {
            //如果不存在 拦截请求，设置状态码为401
            return true;
        }
        SysUserDo user = BeanUtil.fillBeanWithMap(objectMap, new SysUserDo(), false);
        //如果存在将用户存放到ThreadLocal中
        UserHolder.saveUser( user);
        // 刷新过期时间
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
