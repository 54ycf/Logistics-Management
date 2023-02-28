package com.ycf.j2eeclass.controller;

import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.OrderInfo;
import com.ycf.j2eeclass.security.TokenUtil;
import com.ycf.j2eeclass.service.OrderInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/postman")
public class PostmanController {
    
    @Resource
    OrderInfoService orderInfoService;

    /**
     * 查看为完成的订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/orderundo")
    public Result postmanFindOrderUndo(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                   @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestHeader("token") String jwtToken){
        String postmanName = TokenUtil.parseInfo(jwtToken,"username");
        return orderInfoService.postmanFindOrderByState(pageNum,pageSize,postmanName,search,"已分配");
    }

    /**
     * 更新订单状态，变为已签收
     * @param orderInfo
     * @param jwtToken
     * @return
     */
    @PutMapping("/orderundo")
    public Result<?> changeOrderState2Arrived(@RequestBody OrderInfo orderInfo, @RequestHeader("token") String jwtToken){
        orderInfo.setPostmanName(TokenUtil.parseInfo(jwtToken,"username"));// 自己是收件人，token防止恶意操作
        Integer id = orderInfo.getId();
        String postmanName = orderInfo.getPostmanName();
        return orderInfoService.updateOrderState2Arrived(id,postmanName);
    }

    /**
     * 查看已完成的订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/orderdone")
    public Result postmanFindOrderDone(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                   @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestHeader("token") String jwtToken){
        String postmanName = TokenUtil.parseInfo(jwtToken,"username");
        return orderInfoService.postmanFindOrderByState(pageNum,pageSize,postmanName,search,"已送达");
    }

}
