package com.fgrapp.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    public Map<String, Long> getActiveLastYear() {
        Map<String,Long> map = new HashMap<>();
        Cursor<String> uvCursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(RedisConstants.TOPIC_ACTIVE_KEY + "*").build());
        while (uvCursor.hasNext()) {
            String nextKey = uvCursor.next();
            String key = nextKey.substring(RedisConstants.TOPIC_ACTIVE_KEY.length()).replaceAll(":", "-");
            map.put(key,stringRedisTemplate.opsForHyperLogLog().size(nextKey));
        }
        return map;
    }

    /**
     * ??????????????????
     */
    public void addCommit() {
        stringRedisTemplate.opsForZSet().incrementScore(RedisConstants.TOPIC_COMMIT_KEY,DateUtil.format(new Date(), "yyyy-MM-dd"), 1L);
    }

    public Double getCommit(String date) {
        return stringRedisTemplate.opsForZSet().score(RedisConstants.TOPIC_PV_KEY,date);
    }

    public Map<String, Double> getAllCommit() {
        Map<String,Double> map = new HashMap<>();
        Cursor<ZSetOperations.TypedTuple<String>> cursor = stringRedisTemplate.opsForZSet().scan(RedisConstants.TOPIC_COMMIT_KEY, ScanOptions.scanOptions().build());
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<String> next = cursor.next();
            map.put(next.getValue(), next.getScore());
        }
        return map;
    }
    /**
     * ??????Id????????????
     * @param id
     */
    public void deleteById(String id) {
        stringRedisTemplate.delete(RedisConstants.TOPIC_DETAIL_KEY + id);
    }

    /**
     * ????????????
     */
    public void stared(String id) {
        stringRedisTemplate.opsForZSet().add(RedisConstants.TOPIC_LIKED_KEY + id, UserHolder.getUserId().toString(), System.currentTimeMillis());
    }

    /**
     * ????????????
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
     * ??????score
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
     * ????????????
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        //???????????????????????????
        time = unit.toSeconds(time) + (long) (Math.random() * 600);
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(time));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbQueryCall, Long time, TimeUnit unit) {
        //??????redis?????????
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //????????????????????????
        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if ("".equals(json)) {
            return null;
        }
        //????????????????????????????????????
        R r = dbQueryCall.apply(id);
        //???????????????????????????????????????
        if (r == null) {
            //?????????????????????
            set(key,"",time,unit);
            return null;
        }
        //??????????????????????????????redis???
        set(key,r,time,unit);
        //??????
        return r;
    }
    private static final ExecutorService CACHE_EXECUTORS = Executors.newFixedThreadPool(10);

    @Autowired
    @Lazy
    private CacheClient cacheClient;

    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbQueryCall, Long time, TimeUnit unit) {
        //??????redis?????????
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        //???????????????????????????
        if (StrUtil.isBlank(json)) {
            return null;
        }
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        //??????????????????
        if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
            //?????????????????????
            return r;
        }
        //????????? ????????????
        String lockKey = RedisConstants.LOCK_CACHE_KEY + id;
        //???????????????
        boolean flag = tryLock(lockKey);
        if (flag) {
            cacheClient.updateCache(key,lockKey,id,dbQueryCall,time,unit);
        }
        //???????????? ????????????????????????
        //??????
        return r;
    }
    @Async("commonThreadPool")
    public <R, ID> void updateCache(String key,String lockKey, ID id, Function<ID, R> dbQueryCall, Long time, TimeUnit unit) {
        try {
            R r1 = dbQueryCall.apply(id);
            setWithLogicalExpire(key,r1,time,unit);
        } finally {
            unLock(lockKey);
        }
    }
    /**
     * ?????????
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "lock", 20, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * ?????????
     * @param key
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }

    public Set<String> readTopId() {
        return stringRedisTemplate.opsForZSet().reverseRange(RedisConstants.TOPIC_PV_KEY, 0, 4);
    }
}
