package com.fgrapp.config.mybatisplus;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.fgrapp.util.RedisIdWorker;
import org.springframework.stereotype.Component;

/**
 * @author fgr
 * @date 2022-11-05 19:29
 **/
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    private final RedisIdWorker redisIdWorker;

    public CustomIdGenerator(RedisIdWorker redisIdWorker) {
        this.redisIdWorker = redisIdWorker;
    }

    @Override
    public Long nextId(Object entity) {
        //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
        String bizKey = entity.getClass().getSimpleName();
        //根据bizKey调用分布式ID生成
        //返回生成的id值即可.
        return redisIdWorker.nextId(bizKey);
    }
}
