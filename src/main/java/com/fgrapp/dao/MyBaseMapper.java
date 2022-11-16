package com.fgrapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fgr
 * @date 2022-11-06 13:10
 **/
public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入
     * @param batchList 插入列表
     * @return 插入数量
     */
    int insertBatch(@Param("list") List<T> batchList);

}
