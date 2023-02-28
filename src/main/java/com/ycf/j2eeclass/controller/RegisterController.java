package com.ycf.j2eeclass.controller;

import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.User;
import com.ycf.j2eeclass.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    UserService userService;

    @PostMapping
    public Result register(@RequestBody User user){
        return userService.addUser(user);
    }
}
