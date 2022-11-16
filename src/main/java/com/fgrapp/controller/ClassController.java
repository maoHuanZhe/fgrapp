package com.fgrapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.domain.FuncClassDo;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.service.ClassService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fgr
 * @date 2022-11-05 19:04
 **/
@RequestMapping("/func/class")
@ResponseResultBody
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }
    @GetMapping("page")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return classService.getPage(map);
    }

    @GetMapping("/{id}")
    public FuncClassDo getInfo(@PathVariable Long id){
        return classService.getById(id);
    }
    @PostMapping
    public void save(@Validated @RequestBody FuncClassDo info){
        classService.add(info);
    }

    @PutMapping
    public void update(@Validated @RequestBody FuncClassDo info){
        classService.update(info);
    }
}
