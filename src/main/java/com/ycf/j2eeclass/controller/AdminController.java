package com.ycf.j2eeclass.controller;

import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.OrderInfo;
import com.ycf.j2eeclass.security.TokenUtil;
import com.ycf.j2eeclass.service.OrderInfoService;
import com.ycf.j2eeclass.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Resource
    UserService userService;
    @Resource
    OrderInfoService orderInfoService;

    /**
     * 管理员查找所有的用户
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/user")
    public Result findUser(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                           @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                           @RequestParam(defaultValue = "") String search,
                           @RequestHeader("token") String jwtToken){  //关键字
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        return userService.findAllUser(pageNum, pageSize,search,"user");
    }

    /**
     * 管理员查找所有的快递员
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/postman")
    public Result findPostman(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                           @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                           @RequestParam(defaultValue = "") String search,
                           @RequestHeader("token") String jwtToken){  //关键字
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        return userService.findAllUser(pageNum, pageSize,search,"postman");
    }

    /**
     * 删除用户或者员工
     * @param id
     * @return
     */
    @DeleteMapping(value = {"user/{id}","postman/{id}"})
    public Result deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    /**
     * 查看未分配的订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/orderunalloc")
    public Result adminFindOrderUnalloc(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                        @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                        @RequestParam(defaultValue = "") String search,//关键字,寄件人名字
                                        @RequestHeader("token") String jwtToken){
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        return orderInfoService.findAllOrder(pageNum, pageSize,search,"已下单");
    }

    /**
     * 更新订单状态为alloc
     * @param orderInfo
     * @param jwtToken
     * @return
     */
    @PutMapping("/orderunalloc")
    public Result changeOrderState2Alloc(@RequestBody OrderInfo orderInfo, @RequestHeader("token") String jwtToken){
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        Integer id = orderInfo.getId();
        String postmanName = orderInfo.getPostmanName();
        return orderInfoService.updateOrderState2Alloc(id,postmanName);
    }

    /**
     * 查看未分配的订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/orderalloc")
    public Result adminFindOrderAlloc(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                        @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                        @RequestParam(defaultValue = "") String search,//关键字,寄件人名字
                                        @RequestHeader("token") String jwtToken){
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        return orderInfoService.findAllOrder(pageNum, pageSize,search,"已分配");
    }

    @DeleteMapping(value = {"orderunalloc/{id}","orderalloc/{id}"})
    public Result deleteOrder(@PathVariable Long id, @RequestHeader("token")String jwtToken){
        String role = TokenUtil.parseInfo(jwtToken,"role");  // 自己是admin，token防止恶意操作
        if(!(role.equals("admin"))){
            return Result.error("-1","非管理员无法访问");
        }
        return orderInfoService.deleteOrder(id);
    }

}
