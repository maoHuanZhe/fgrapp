package com.fgrapp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgrapp.dao.ClassMapper;
import com.fgrapp.domain.FuncClassDo;
import com.fgrapp.result.ResultException;
import com.fgrapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-05 18:58
 **/
@Service
public class ClassService extends ServiceImpl<ClassMapper, FuncClassDo> {

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map, FuncClassDo.class),map);
    }

    public void add(FuncClassDo info) {
        baseMapper.insert(info);
    }

    public void update(FuncClassDo info) {
        baseMapper.updateById(info);
    }

    public List<FuncClassDo> getList() {



        List<FuncClassDo> list = baseMapper.selectList(null);

        return list;
    }
}
