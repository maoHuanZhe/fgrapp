package com.fgrapp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgrapp.dao.CommentMapper;
import com.fgrapp.domain.CommentDo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-12 13:07
 **/
@Service
public class CommentService extends ServiceImpl<CommentMapper, CommentDo> {

    public List<Map<String, Object>> getListByContextId(String id) {
        return baseMapper.getListByContextId(id);
    }
}
