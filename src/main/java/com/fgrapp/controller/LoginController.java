package com.fgrapp.controller;

import com.fgrapp.domain.vo.RegisterVo;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.service.LoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fgr
 * @date 2022-11-01 20:19
 **/
@Validated
@RequestMapping("/login")
@ResponseResultBody
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/{phoneOrEmail}")
    public void sendCode(@PathVariable String phoneOrEmail){
        loginService.sendCode(phoneOrEmail);
    }
    @PostMapping()
    public String registerOfEmail(@RequestBody RegisterVo info){
        return loginService.register(info);
    }

    @PostMapping("logout")
    public void logout(){
        loginService.logout();
    }
}
