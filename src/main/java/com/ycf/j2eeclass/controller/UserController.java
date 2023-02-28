package com.ycf.j2eeclass.controller;

import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.AddrInfo;
import com.ycf.j2eeclass.entity.OrderInfo;
import com.ycf.j2eeclass.security.TokenUtil;
import com.ycf.j2eeclass.service.AddrInfoService;
import com.ycf.j2eeclass.service.OrderInfoService;
import com.ycf.j2eeclass.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Resource
    OrderInfoService orderInfoService;
    @Resource
    UserService userService;
    @Resource
    AddrInfoService addrInfoService;

    /**
     * 下单的时候检测收件人是否存在
     * @param you
     * @return
     */
    @GetMapping("/send")
    public Result isUserExist(@RequestParam(defaultValue = "") String you){
        return userService.isUserExist(you);
    }

    /**
     * 下单新订单
     * @param orderInfo
     * @param jwtToken
     * @return
     */
    @PostMapping("/send")
    public Result send(@RequestBody OrderInfo orderInfo, @RequestHeader("token") String jwtToken){
        if(userService.isUserExist(orderInfo.getReceiverName()).getCode().equals("-2")){
            return Result.error("-1","下单错误，请输入正确收件人用户名");
        }
        orderInfo.setSenderName(TokenUtil.parseInfo(jwtToken,"username"));// 自己是寄件人，token防止恶意操作
        return orderInfoService.addOrder(orderInfo);
    }

    /**
     * 查询自己的寄件订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/ordersend")  //Get查询
    public Result<?> senderFindOrder(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                  @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestHeader("token") String jwtToken){  //关键字
        String senderName = TokenUtil.parseInfo(jwtToken,"username");  // 自己是寄件人，token防止恶意操作
        return orderInfoService.senderFindOrder(pageNum,pageSize,senderName,search);
    }

    /**
     * 查询自己的收件订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param jwtToken
     * @return
     */
    @GetMapping("/orderreceive")
    public Result<?> receiverFindOrder(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                     @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestHeader("token") String jwtToken){  //关键字
        String receiverName = TokenUtil.parseInfo(jwtToken,"username");  // 自己是收件人，token防止恶意操作
        return orderInfoService.receiverFindOrder(pageNum,pageSize,search,receiverName);
    }

    /**
     * 更新订单状态，变为已签收
     * @param orderInfo
     * @param jwtToken
     * @return
     */
    @PutMapping("/orderreceive")
    public Result<?> changeOrderState2Signed(@RequestBody OrderInfo orderInfo, @RequestHeader("token") String jwtToken){
        String receiverName = TokenUtil.parseInfo(jwtToken,"username");// 自己是收件人，token防止恶意操作
        Integer id = orderInfo.getId();
        return orderInfoService.updateOrderState2Signed(id, receiverName);
    }

    /**
     * 查询地址簿
     * @param pageNum
     * @param pageSize
     * @param status
     * @param contacts
     * @param jwtToken
     * @return
     */
    @GetMapping("/addr")
    public Result<?> findPageAddr(@RequestParam(defaultValue = "1") Integer pageNum, //当前页
                                  @RequestParam(defaultValue = "10") Integer pageSize, //每页多少条
                                  @RequestParam(defaultValue = "1") Integer status, //要查寄件还是收件
                                  @RequestParam(defaultValue = "") String contacts,
                                  @RequestHeader("token") String jwtToken){  //关键字
        Integer userId = Integer.valueOf(TokenUtil.parseInfo(jwtToken,"id"));  // 自己的真实id权限
        return addrInfoService.findAddrByStatus(pageNum,pageSize,status,contacts,userId);
    }

    /**
     * 新增地址簿
     * @param addrInfo
     * @param jwtToken
     * @return
     */
    @PostMapping("/addr")
    public Result addAddr(@RequestBody AddrInfo addrInfo, @RequestHeader("token")String jwtToken){
        addrInfo.setUserId(Integer.valueOf(TokenUtil.parseInfo(jwtToken,"id")));
        return addrInfoService.addAddr(addrInfo);
    }

    /**
     * 更新地址簿
     * @param addrInfo
     * @param jwtToken
     * @return
     */
    @PutMapping("/addr")
    public Result changeMyAddrInfo(@RequestBody AddrInfo addrInfo, @RequestHeader("token") String jwtToken){
        addrInfo.setUserId(Integer.valueOf(TokenUtil.parseInfo(jwtToken,"id")));
        return addrInfoService.updateAddr(addrInfo);
    }

    /**
     * 根据订单id删除订单
     * @param id
     * @param jwtToken
     * @return
     */
    @DeleteMapping("/addr/{id}")
    public Result deleteTheAddrInfo(@PathVariable("id") Long id, @RequestHeader("token") String jwtToken){
        Integer userId = Integer.valueOf(TokenUtil.parseInfo(jwtToken,"id"));
        return addrInfoService.deleteTheAddr(id,userId);
    }

}
