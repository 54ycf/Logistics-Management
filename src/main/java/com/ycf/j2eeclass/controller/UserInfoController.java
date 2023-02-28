package com.ycf.j2eeclass.controller;

import cn.hutool.json.JSONObject;
import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.User;
import com.ycf.j2eeclass.security.TokenUtil;
import com.ycf.j2eeclass.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserInfoController {

    @Resource
    UserService userService;

    /**
     * 查看用户个人信息
     * @param jwtToken
     * @return
     */
    @GetMapping(value = {"/info","/user/info","/postman/info"})
    public Result lookMyInfo(@RequestHeader("token") String jwtToken){
        Integer userId = Integer.parseInt(TokenUtil.parseInfo(jwtToken,"id"));  //用token验证自己真实id
        return userService.findUserById(userId);
    }

    /**
     * 更新个人信息
     * @param user
     * @param jwtToken
     */
    @PutMapping(value = {"/info","/user/info","/postman/info"})
    public Result updateMyInfo(@RequestBody User user, @RequestHeader("token") String jwtToken){
        user.setId(Integer.parseInt(TokenUtil.parseInfo(jwtToken,"id")));//用token验证自己真实id
        user.setUsername(TokenUtil.parseInfo(jwtToken,"username"));
        user.setRole(TokenUtil.parseInfo(jwtToken,"role")); //防止用户恶意篡改自己身份
        return userService.updateUserInfo(user);
    }

    /**
     * 更新密码
     * @param json
     * @param jwtToken
     * @return
     */
    @PutMapping("/pwd")
    public Result updatePwd(@RequestBody JSONObject json, @RequestHeader("token") String jwtToken){
        Integer id = Integer.valueOf(TokenUtil.parseInfo(jwtToken,"id"));
        String oldPwd = json.getStr("oldPwd");
        String newPwd = json.getStr("newPwd");
        String conPwd = json.getStr("conPwd");
        return userService.updatePwd(oldPwd,newPwd,conPwd,id);
    }
}
