package com.fgrapp.controller;

import com.fgrapp.domain.FuncTopicDo;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.service.TopicService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fgr
 * @date 2022-11-05 18:08
 **/
@RequestMapping("func")
@ResponseResultBody
public class TopicController {
    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public FuncTopicDo getInfo(@PathVariable String id){
        return service.getInfo(id);
    }
    @PostMapping
    public void save(@Validated @RequestBody FuncTopicDo info){
        service.add(info);
    }
    @PutMapping
    public void update(@Validated @RequestBody FuncTopicDo info){
        service.update(info);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(id);
    }

    @PutMapping("/liked/{id}")
    public void liked(@PathVariable String id){
        service.liked(id);
    }
}
