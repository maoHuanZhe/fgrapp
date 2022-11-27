package com.fgrapp.sitemap;

import com.fgrapp.dao.TopicMapper;
import com.fgrapp.domain.FuncTopicDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author fgr
 * @date 2022-11-27 10:46
 **/
@Slf4j
@Configuration
@EnableScheduling
public class SiteMapTask {

    private final TopicMapper topicMapper;

    public SiteMapTask(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    /**
     * 每天凌晨0点执行
     */
    @Async("scheduleThreadPool")
    @Scheduled(cron = "0 0 0 * * ?")
    @PostConstruct
    public void configureTasks() {
        log.info("定时任务开始");
        List<FuncTopicDo> list = topicMapper.getList();
        SiteMapUtil.generator(list);

        log.info("定时任务结束");
    }
}
