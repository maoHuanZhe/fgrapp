package com.fgrapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.domain.FuncTopicDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-05 18:14
 **/
@Component
public interface TopicMapper extends BaseMapper<FuncTopicDo> {
    IPage<Map<String, Object>> getPage(Page<FuncTopicDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);

    List<FuncTopicDo> getList();
}
