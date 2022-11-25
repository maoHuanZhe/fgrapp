package com.fgrapp.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long getLikeNum(String id) {
        return stringRedisTemplate.opsForZSet().zCard(RedisConstants.TOPIC_LIKED_KEY + id);
    }

    public void addPv(Object id) {
        stringRedisTemplate.opsForZSet().incrementScore(RedisConstants.TOPIC_PV_KEY,String.valueOf(id), 1L);
    }

    public Double getPv(Object id) {
        return stringRedisTemplate.opsForZSet().score(RedisConstants.TOPIC_PV_KEY,id);
    }

    public long getAllPv() {
        long pv = 0L;
        Cursor<ZSetOperations.TypedTuple<String>> pvCursor = stringRedisTemplate.opsForZSet().scan(RedisConstants.TOPIC_PV_KEY, ScanOptions.scanOptions().build());
        while (pvCursor.hasNext()) {
            pv += pvCursor.next().getScore().longValue();
        }
        return pv;
    }

    public void addUv(Object id) {
        stringRedisTemplate.opsForHyperLogLog().add(RedisConstants.TOPIC_UV_KEY + id,IpUtils.getIpAddr());
    }

    public Long getUv(Object id) {
        return stringRedisTemplate.opsForHyperLogLog().size(RedisConstants.TOPIC_UV_KEY + id);
    }

    public long getAllUv() {
        long uv = 0L;
        Cursor<String> uvCursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(RedisConstants.TOPIC_UV_KEY + "*").build());
        while (uvCursor.hasNext()) {
            uv += stringRedisTemplate.opsForHyperLogLog().size(uvCursor.next());
        }
        return uv;
    }

    public void addActive() {
        stringRedisTemplate.opsForHyperLogLog().add(RedisConstants.TOPIC_ACTIVE_KEY + DateUtil.format(new Date(), "yyyy:MM:dd"), IpUtils.getIpAddr());
    }

    /**
     * 根据Id删除内容
     * @param id
     */
    public void deleteById(String id) {
        stringRedisTemplate.delete(RedisConstants.TOPIC_DETAIL_KEY + id);
    }

    /**
     * 点赞操作
     */
    public void stared(String id) {
        stringRedisTemplate.opsForZSet().add(RedisConstants.TOPIC_LIKED_KEY + id, UserHolder.getUserId(), System.currentTimeMillis());
    }

    /**
     * 取消点赞
     * @param id
     */
    public void unStared(String id) {
        stringRedisTemplate.opsForZSet().remove(RedisConstants.TOPIC_LIKED_KEY + id,UserHolder.getUserId());
    }

    public long getAllStared() {
        long likedNum = 0L;
        Cursor<String> cursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(RedisConstants.TOPIC_LIKED_KEY + "*").build());
        while (cursor.hasNext()) {
            likedNum += stringRedisTemplate.opsForZSet().zCard(cursor.next());
        }
        return likedNum;
    }
    /**
     * 获取score
     * @param key
     * @param member
     * @return
     */
    public Double getScore(String key, String member) {
        return stringRedisTemplate.opsForZSet().score(key, member);
    }

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 逻辑过期
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        //过期时间加上随机值
        time = unit.toSeconds(time) + (long) (Math.random() * 600);
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(time));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbQueryCall, Long time, TimeUnit unit) {
        //先从redis中获取
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //存在的话直接返回
        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if ("".equals(json)) {
            return null;
        }
        //不存在的话从数据库中获取
        R r = dbQueryCall.apply(id);
        //数据库中不存在的话返回异常
        if (r == null) {
            //缓存中存储空值
            set(key,"",time,unit);
            return null;
        }
        //数据库中存在的话存入redis中
        set(key,r,time,unit);
        //返回
        return r;
    }
    private static final ExecutorService CACHE_EXECUTORS = Executors.newFixedThreadPool(10);

    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbQueryCall, Long time, TimeUnit unit) {
        //先从redis中获取
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //不存在的话直接返回
        if (StrUtil.isBlank(json)) {
            return null;
        }
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        //判断过期时间
        if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
            //未过期直接返回
            return r;
        }
        //已过期 缓存重建
        String lockKey = RedisConstants.LOCK_CACHE_KEY + id;
        //获取互斥锁
        boolean flag = tryLock(lockKey);
        if (flag) {
            //获取成功 开启新的线程 重建缓存
            CACHE_EXECUTORS.submit(() -> {
                try {
                    R r1 = dbQueryCall.apply(id);
                    setWithLogicalExpire(key,r1,time,unit);
                } finally {
                    unLock(lockKey);
                }
            });
        }
        //获取失败 直接返回过期数据
        //返回
        return r;
    }
    /**
     * 获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "lock", 20, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放锁
     * @param key
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }

}
