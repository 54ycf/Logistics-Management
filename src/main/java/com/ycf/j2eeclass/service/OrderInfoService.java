package com.ycf.j2eeclass.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.OrderInfo;
import com.ycf.j2eeclass.mapper.OrderInfoMapper;
import com.ycf.j2eeclass.util.OrderCodeFactory;
import com.ycf.j2eeclass.util.TrimAll;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Service
public class OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 寄件人分页查询用户订单
     * @param pageNum
     * @param pageSize
     * @param senderName
     * @param search
     * @return
     */
    public Result senderFindOrder(Integer pageNum, Integer pageSize, String senderName, String search){

        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getSenderName,senderName);
        if(!StrUtil.isEmpty(search)){ //说明搜索框不为空
            wrapper.like(OrderInfo::getReceiverName,search);
        }
        Page<OrderInfo> orderInfoPagePage = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(orderInfoPagePage);
    }

    /**
     * 收件人分页查询用户订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param receiverName
     * @return
     */
    public Result receiverFindOrder(Integer pageNum, Integer pageSize, String search, String receiverName){
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getReceiverName,receiverName);
        if(!StrUtil.isEmpty(search)){ //说明搜索框不为空
            wrapper.like(OrderInfo::getReceiverName,search);
        }
        Page<OrderInfo> orderInfoPagePage = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(orderInfoPagePage);
    }

    /**
     * 订单已送达时，收件人将订单改为已签收
     * @param id
     * @param receiverName
     * @return
     */
    public Result updateOrderState2Signed(Integer id, String receiverName){
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery()
                .eq(OrderInfo::getReceiverName,receiverName)   //防止此处恶意修改了自己的id
                .eq(OrderInfo::getId,id);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        if(orderInfo!=null && orderInfo.getPackageState().equals("已送达")){
            orderInfo.setReceiveTime(new Date());
            orderInfo.setPackageState("已签收");
            orderInfoMapper.updateById(orderInfo);
            return Result.success();
        }
        return Result.error("-1","请求权限越界");
    }

    /**
     * 新增订单
     * @param orderInfo
     * @return
     */
    public Result addOrder(OrderInfo orderInfo){
        if(orderInfo.getReceiverName().equals(orderInfo.getSenderName())){
            return Result.error("-1","不能寄给自己");
        }
        Date date = new Date();
        orderInfo.setSendTime(date);
        orderInfo.setOrderNum(OrderCodeFactory.getOrderCode(orderInfo.getId(),date));
        orderInfo.setPackagePrice(BigDecimal.valueOf(new Random().nextDouble()*10+5));
        orderInfo.setPackageState("已下单");
        TrimAll.trim(OrderInfo.class,orderInfo); //消除收尾空格
        orderInfoMapper.insert(orderInfo);
        return Result.success();
    }

    /**
     * 快递员查找订单，根据分类和寄件人为关键字
     * @param pageNum
     * @param pageSize
     * @param postmanName
     * @param search
     * @return
     */
    public Result postmanFindOrderByState(Integer pageNum, Integer pageSize, String postmanName, String search, String state){
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getPostmanName, postmanName);
        if(!StrUtil.isEmpty(search)){ //说明搜索框不为空
            wrapper.like(OrderInfo::getSenderName,search);
        }
        switch (state){
            case "已分配":
                wrapper = wrapper.eq(OrderInfo::getPackageState,"已分配");
                break;
            case "已送达":
                wrapper = wrapper.eq(OrderInfo::getPackageState,"已送达").or().eq(OrderInfo::getPackageState,"已签收");
                break;
            default:
                break;
        }
        Page<OrderInfo> orderInfoPagePage = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(orderInfoPagePage);
    }

    /**
     * 订单已送达时，收件人将订单改为已签收
     * @param id
     * @param postmanName
     * @return
     */
    public Result updateOrderState2Arrived(Integer id, String postmanName){
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery()
                .eq(OrderInfo::getPostmanName,postmanName)   //防止此处恶意修改了自己的id
                .eq(OrderInfo::getId,id);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        if(orderInfo!=null && orderInfo.getPackageState().equals("已分配")){
            orderInfo.setPackageState("已送达");
            orderInfoMapper.updateById(orderInfo);
            return Result.success();
        }
        return Result.error("-1","请求权限越界");
    }

    /**
     * 管理员查找所有订单
     * @param pageNum
     * @param pageSize
     * @param search
     * @param packageState
     * @return
     */
    public Result findAllOrder(Integer pageNum, Integer pageSize, String search, String packageState){
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery();
//        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getPackageState,packageState);
        if(!StrUtil.isEmpty(search)){ //说明搜索框不为空
            wrapper.like(OrderInfo::getSenderName,search);
        }
        switch (packageState){
            case "已下单":
                wrapper = wrapper.eq(OrderInfo::getPackageState,"已下单");
                break;
            case "已分配":
                wrapper = wrapper.eq(OrderInfo::getPackageState,"已分配").or().eq(OrderInfo::getPackageState,"已送达").or().eq(OrderInfo::getPackageState,"已签收");
                break;
            default:
                break;
        }
        Page<OrderInfo> orderInfoPagePage = orderInfoMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(orderInfoPagePage);
    }

    /**
     * 订单订单状态改为已分配
     * @param id
     * @param postmanName
     * @return
     */
    public Result updateOrderState2Alloc(Integer id, String postmanName){
        if (id == null || postmanName == null){
            return Result.error("-1","请分配配送人");
        }
        OrderInfo orderInfo = orderInfoMapper.selectById(id);
        orderInfo.setPostmanName(postmanName);
        orderInfo.setPackageState("已分配");
        orderInfoMapper.updateById(orderInfo);
        return Result.success();
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    public Result deleteOrder(Long id){
        orderInfoMapper.deleteById(id);
        return Result.success();
    }
}
