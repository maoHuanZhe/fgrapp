package com.fgrapp.pv;

import com.fgrapp.util.CacheClient;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * @author fgr
 * @date 2022-11-14 20:23
 **/
@Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    private final CacheClient cacheClient;

    public PageViewAspect(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.fgrapp.pv.PageView)")
    public void pageViewAspect() {}

    /**
     * 切入处理
     */
    @AfterReturning(value = "pageViewAspect()")
    public void after(JoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        Object id = object[0];
        log.info("id:{}", id);
        // 缓存 PV
        cacheClient.addPv(id);
        // 缓存 UV
        cacheClient.addUv(id);
        // 缓存 日活
        cacheClient.addActive();
    }
}


