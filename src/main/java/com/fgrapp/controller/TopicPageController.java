package com.fgrapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.domain.FuncClassDo;
import com.fgrapp.domain.FuncTopicDo;
import com.fgrapp.pv.PageView;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.service.ClassService;
import com.fgrapp.service.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-05 18:08
 **/
@RequestMapping("page")
@ResponseResultBody
public class TopicPageController {

    private final TopicService service;
    private final ClassService classService;
    public TopicPageController(TopicService service, ClassService classService) {
        this.service = service;
        this.classService = classService;
    }

    @GetMapping()
    public IPage<Map<String,Object>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }
    @GetMapping("/list")
    public List<FuncTopicDo> getList() {
        return service.getList();
    }
    @GetMapping("/classList")
    public List<FuncClassDo> list(){
        return classService.getList();
    }
    @PageView(prefix = "topic:")
    @GetMapping("/detail/{id}")
    public Map<String,Object> getDetailInfo(@PathVariable String id){
        return service.getDetailInfo(id);
    }
    @GetMapping("/num")
    public Map<String,Object> getNum() {
        return service.getNum();
    }
    @GetMapping("/randomList")
    public List<FuncTopicDo> randomList(FuncTopicDo info){
        return service.randomList(info);
    }
    @GetMapping("/readTop")
    public List<FuncTopicDo> readTop(){
        return service.readTop();
    }
}
