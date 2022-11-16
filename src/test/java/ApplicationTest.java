import com.fgrapp.Application;
import com.fgrapp.dao.TopicMapper;
import com.fgrapp.service.TopicService;
import com.fgrapp.util.CacheClient;
import com.fgrapp.util.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * @author fgr
 * @date 2022-11-15 13:05
 **/
@SpringBootTest
@ContextConfiguration(classes = Application.class
)
public class ApplicationTest {
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private CacheClient cacheClient;

    @Test
    void test() {

        topicMapper.selectList(null).forEach(item -> {
            cacheClient.setWithLogicalExpire(RedisConstants.TOPIC_DETAIL_KEY+item.getId(),item,RedisConstants.TOPIC_DETAIL_TTL, TimeUnit.MINUTES);
        });
    }
}
