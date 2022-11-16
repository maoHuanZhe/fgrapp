package com.fgrapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fgrapp.domain.CommentDo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-12 13:07
 **/
@Component
public interface CommentMapper extends BaseMapper<CommentDo> {
    List<Map<String, Object>> getListByContextId(String id);
}
