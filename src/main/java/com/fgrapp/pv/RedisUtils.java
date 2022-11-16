package com.fgrapp.pv;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fgr
 * @date 2022-11-14 20:26
 **/
@Component
public class RedisUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取缓存
     * @param key
     */
    public Object get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取缓存
     * @param keys
     */
    public List<String> mGet(List<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        stringRedisTemplate.delete(key[0]);
    }

    /**
     * HyperLogLog计数
     * @param key
     * @param value
     */
    public Long add(String key, String... value) {
        return stringRedisTemplate.opsForHyperLogLog().add(key,value);
    }

    /**
     * HyperLogLog获取总数
     * @param key
     */
    public Long count(String key) {
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * HyperLogLog获取总数
     * @param key
     */
    public Long count(String... key) {
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }
}

