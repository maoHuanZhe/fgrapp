package com.fgrapp.limit;

import com.fgrapp.result.ResultException;
import com.fgrapp.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.lang.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.List;


/**
 * @author fgr
 * @date 2022-11-20 09:21
 **/
@Aspect
@Configuration
@Slf4j
@Order(1)
public class LimitAspect {

    @Value("${limit.interval}")
    private String interval;
    @Value("${limit.maxInInterval}")
    private String maxInInterval;
    @Value("${limit.minDifference:0}")
    private String minDifference;
    @Value("${limit.addNewTimestamp:false}")
    private String addNewTimestamp;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("lua/rollingRateLimiter.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }
    /**
     * 切入点
     */
    @Pointcut("@annotation(com.fgrapp.limit.Limit)")
    public void limitAspect() {}

    @Around(value = "limitAspect() && @annotation(limit)")
    public Object around(ProceedingJoinPoint joinPoint, Limit limit) {
        Object obj = null;
        try {
            String zSetName = limit.prefix() + IpUtils.getIpAddr();
            String uuid = UUID.fastUUID().toString(true);
            long now = System.currentTimeMillis();
            //执行lua脚本
            Long result = stringRedisTemplate.execute(
                    SECKILL_SCRIPT,
                    Arrays.asList(zSetName, uuid, String.valueOf(now)),
                    interval, maxInInterval, minDifference, addNewTimestamp
            );
            int r = result.intValue();
            if (r == -1) {
                log.error("小同志，你访问的太频繁了,result:{}",r);
                throw new ResultException("小同志，你访问的太频繁了");
            } else if (r == -2) {
                log.error("小同志，你访问的太频繁了,result:{}",r);
                throw new ResultException("小同志，你访问的太频繁了");
            } else if (r == 1) {
                log.info("通过限流");
                obj = joinPoint.proceed();
            }
        } catch (ResultException resultException) {
            throw resultException;
        } catch (Throwable throwable) {
            log.error("小同志，你访问的太频繁了",throwable);
            throw new ResultException("小同志，你访问的太频繁了");
        }
        return obj;
    }
}
