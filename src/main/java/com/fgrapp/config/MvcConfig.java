package com.fgrapp.config;

import com.fgrapp.interceptor.LoginInterceptor;
import com.fgrapp.interceptor.RefreshTokenInterceptor;
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

    public MvcConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/login",
                        "/login/**",
                        "/page",
                        "/page/**"
                ).order(1);
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**").order(0);
    }
}
