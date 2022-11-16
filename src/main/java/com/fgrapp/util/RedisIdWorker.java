package com.fgrapp.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author fgr
 * @date 2022-11-05 19:25
 **/
@Component
public class RedisIdWorker {
    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 2022-01-01 00:00:00 的时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 序列号位数
     */
    private static final int COUNT_BITS = 32;

    public long nextId(String keyPrefix) {
        //获取时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        //获取序列号
        //获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue().increment("inc:" + keyPrefix + ":" + date);
        //拼接并返回
        return timestamp << COUNT_BITS | count;
    }

}
