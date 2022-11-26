package com.fgrapp.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgrapp.dao.TopicMapper;
import com.fgrapp.domain.FuncTopicDo;
import com.fgrapp.domain.SysUserDo;
import com.fgrapp.result.ResultException;
import com.fgrapp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author fgr
 * @date 2022-11-05 18:15
 **/
@Slf4j
@Service
public class TopicService extends ServiceImpl<TopicMapper, FuncTopicDo> {

    private final StringRedisTemplate stringRedisTemplate;
    private final SysUserService userService;
    private final CommentService commentService;
    @Autowired
    private CacheClient cacheClient;

    public TopicService(StringRedisTemplate stringRedisTemplate, SysUserService userService, CommentService commentService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.commentService = commentService;
    }

    public IPage<Map<String, Object>> getPage(Map<String, Object> map) {
        IPage<Map<String, Object>> listIPage = baseMapper.getPage(PageUtil.getParamPage(map, FuncTopicDo.class), map);
        List<Map<String, Object>> records = listIPage.getRecords();
        records.forEach(item -> setNum(item, (String) item.get("id")));
        return listIPage;
    }

    public void update(FuncTopicDo info) {
        //获取博客摘要
        String summary = MDToText.getSummary(info.getAnswer());
        info.setSummary(summary);
        info.setLastUpdateTime(null);
        //修改问题
        baseMapper.updateById(info);
        //更新缓存
        cacheClient.setWithLogicalExpire(RedisConstants.TOPIC_DETAIL_KEY + info.getId(),info,RedisConstants.TOPIC_DETAIL_TTL, TimeUnit.MINUTES);
    }

    public void delete(String id) {
        baseMapper.deleteById(id);
        //删除缓存
        cacheClient.deleteById(id);
    }

    public void add(FuncTopicDo info) {
        //获取博客摘要
        String summary = MDToText.getSummary(info.getAnswer());
        info.setSummary(summary);
        //新增问题
        baseMapper.insert(info);
        //创建缓存
        cacheClient.setWithLogicalExpire(RedisConstants.TOPIC_DETAIL_KEY + info.getId(),info,RedisConstants.TOPIC_DETAIL_TTL, TimeUnit.MINUTES);
    }

    public FuncTopicDo getInfo(String id) {
        //获取基本信息
        return baseMapper.selectById(id);
    }

    public Map<String, Object> getDetailInfo(String id) {
        Map<String,Object> map = new HashMap<>(6);
        FuncTopicDo topicDo = cacheClient.queryWithLogicalExpire(RedisConstants.TOPIC_DETAIL_KEY, id,
                FuncTopicDo.class, this::getInfo,
                RedisConstants.TOPIC_DETAIL_TTL, TimeUnit.MINUTES);
        if (topicDo == null) {
            log.error("内容不存在,id:{}", id);
            throw new ResultException("内容不存在");
        }
        map.put("topic",topicDo);
        map.put("canLike",canLike(id));
        map.put("likedUsers",getLikedUser(id));
        map.put("comments",commentService.getListByContextId(id));
        setNum(map,id);
        return map;
    }

    private void setNum(Map<String,Object> map, String id){
        map.put("likeNum",cacheClient.getLikeNum(id));
        map.put("uv",cacheClient.getUv(id));
        map.put("pv",cacheClient.getPv(id));
    }

    public void liked(String id) {
        if (!canLike(id)) {
            //如果已经点赞了 则取消点赞
            cacheClient.unStared(id);
        } else {
            //如果没有点赞 则进行点赞
            cacheClient.stared(id);
        }
    }

    private boolean canLike(String id) {
        String userId = UserHolder.getUserId();
        if (userId == null) {
            return true;
        }
        Double score = cacheClient.getScore(RedisConstants.TOPIC_LIKED_KEY + id, userId);
        return score == null;
    }

    public List<FuncTopicDo> getList() {
        return baseMapper.getList();
    }

    public List<SysUserDo> getLikedUser(String id) {
        String key = RedisConstants.TOPIC_LIKED_KEY + id;
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5 == null || top5.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        return userService
                .query().in("id", ids)
                .last("order by field(id," + idStr + ")").list();
    }

    public Map<String, Object> getNum() {
        Map<String, Object> map = new HashMap<>();
        map.put("comments", commentService.count());
        map.put("uv", cacheClient.getAllUv());
        map.put("pv", cacheClient.getAllPv());
        map.put("liked", cacheClient.getAllStared());
        return map;
    }

    public List<FuncTopicDo> randomList(FuncTopicDo info) {
        return baseMapper.getRandomIdList(info);
    }

    public List<FuncTopicDo> readTop() {
        Set<String> top5 = cacheClient.readTopId();
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        return query().in("id", ids)
                .last("order by field(id," + idStr + ")").list();
    }
}
