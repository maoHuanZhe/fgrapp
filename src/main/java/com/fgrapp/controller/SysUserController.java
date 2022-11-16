package com.fgrapp.controller;

import com.fgrapp.domain.SysUserDo;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.util.UserHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fgr
 * @date 2022-11-05 10:43
 **/
@RequestMapping("/user")
@ResponseResultBody
public class SysUserController {

    @GetMapping("/getInfo")
    public SysUserDo getInfo() {
        return UserHolder.getUser();
    }
}
