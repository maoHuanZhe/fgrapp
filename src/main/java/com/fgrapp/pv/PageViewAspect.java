package com.fgrapp.pv;

import cn.hutool.core.date.DateUtil;
import com.fgrapp.result.Result;
import com.fgrapp.result.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author fgr
 * @date 2022-11-14 20:23
 **/
@Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    final String PV = "pv:";
    final String UV = "uv:";
    final String UV_DAY = "uv:day:";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取当前的ServletRequest
     * @return
     */
    protected HttpServletRequest servletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.fgrapp.pv.PageView)")
    public void pageViewAspect() {}

    /**
     * 切入处理
     * @param joinPoint
     * @return
     */
    @AfterReturning(value = "pageViewAspect() && @annotation(pageView)", returning = "jsonResult")
    public void around(JoinPoint joinPoint, PageView pageView, Object jsonResult) {
        log.info("@PageView请求返回内容：RESPONSE : {}", Objects.nonNull(jsonResult) ? jsonResult.toString() : "");
        // 先判断 pageView 是否为空, 为空则获取类上注解
        if (pageView == null) {
            Class<?> aClass = joinPoint.getTarget().getClass();
            pageView = AnnotationUtils.findAnnotation(aClass, PageView.class);
        }
        Object[] object = joinPoint.getArgs();
        Object id = object[0];
        log.info("id:{}", id);
        this.cache(pageView,id);
    }


    /**
     * 缓存 主维度
     * @param pageView
     * @param id
     */
    private void cache(PageView pageView,Object id){
        // 缓存 PV
        redisUtils.incr(pageView.prefix() + PV + id,1);
        // 缓存 UV
        redisUtils.add(pageView.prefix() + UV + id, IpUtils.getIpAddr(servletRequest()));
        // 缓存 UV 日访客
        redisUtils.add(pageView.prefix() + UV_DAY + id, IpUtils.getIpAddr(servletRequest())
                + "-" + DateUtil.format(new Date(), "yyyyMMdd"));
    }

}


