package com.fgrapp.config;

import com.fgrapp.interceptor.LoginInterceptor;
import com.fgrapp.interceptor.RefreshTokenInterceptor;
import com.fgrapp.log.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fgr
 * @date 2022-11-05 10:30
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;

    private final LogInterceptor logInterceptor;

    public MvcConfig(StringRedisTemplate stringRedisTemplate, LogInterceptor logInterceptor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/login",
                        "/login/**",
                        "/page",
                        "/page/**"
                ).order(2);
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(1);
        registry.addInterceptor(logInterceptor).order(0);
    }
}
