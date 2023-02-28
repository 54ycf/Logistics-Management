package com.ycf.j2eeclass.controller;

import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.User;
import com.ycf.j2eeclass.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    UserService userService;

    @PostMapping
    public Result<?> login(@RequestBody User user){
        return userService.checkUser(user); //data中返回了token
    }

}
