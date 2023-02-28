package com.ycf.j2eeclass.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.AddrInfo;
import com.ycf.j2eeclass.mapper.AddrInfoMapper;
import com.ycf.j2eeclass.util.JudgeEmpty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddrInfoService {

    @Resource
    private AddrInfoMapper addrInfoMapper;

    /**
     * 添加修改时检测该地址簿的合法性，重复，权限等等
     * @param addrInfo
     * @return
     */
    private String checkAddrInfo(AddrInfo addrInfo){
        String msg = JudgeEmpty.isBlank(AddrInfo.class, addrInfo); //利用反射判断是否有元素为null
        if(!msg.equals("ok")){
            return msg;
        }
        Integer status = addrInfo.getStatus();
        if (!(status==1|| status==2)){
            return "请选择是收件人或寄件人";
        }
        AddrInfo test = addrInfoMapper.selectOne(Wrappers.<AddrInfo>lambdaQuery()
                .eq(AddrInfo::getUserId,addrInfo.getUserId())
                .eq(AddrInfo::getStatus,status)
                .eq(AddrInfo::getContacts,addrInfo.getContacts())
                .eq(AddrInfo::getPhoneNum,addrInfo.getPhoneNum())
                .eq(AddrInfo::getAddr,addrInfo.getAddr()));
        if(test!=null){
            return "已有重复地址";
        }
        return "ok";
    }

    /**
     * 分页查询地址簿
     * @param pageNum
     * @param pageSize
     * @param status
     * @param contacts
     * @param userId
     * @return
     */
    public Result findAddrByStatus(Integer pageNum, Integer pageSize, Integer status, String contacts, Integer userId){
        if (!(status==1|| status==2)){
            return Result.error("-1","请选择是收件人或寄件人");
        }
        LambdaQueryWrapper<AddrInfo> wrapper = Wrappers.<AddrInfo>lambdaQuery().eq(AddrInfo::getUserId,userId).eq(AddrInfo::getStatus,status);
        if(!StrUtil.isEmpty(contacts)){ //说明搜索框不为空
            wrapper.like(AddrInfo::getContacts,contacts);
        }
        Page<AddrInfo> addrInfoPage = addrInfoMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(addrInfoPage);
    }

    /**
     * 新增地址簿
     * @param addrInfo
     * @return
     */
    public Result addAddr(AddrInfo addrInfo){
        String msg = checkAddrInfo(addrInfo);
        if (!msg.equals("ok")){
            return Result.error("-1",msg);
        }
        addrInfoMapper.insert(addrInfo);
        return Result.success();
    }

    /**
     * 更新地址簿
     * @param addrInfo
     * @return
     */
    public Result updateAddr(AddrInfo addrInfo){
        String msg = checkAddrInfo(addrInfo);
        if (!msg.equals("ok")){
            return Result.error("-1",msg);
        }
        addrInfoMapper.updateById(addrInfo);
        return Result.success();
    }

    /**
     * 删除地址
     * @param id
     * @param userId
     * @return
     */
    public Result deleteTheAddr(Long id, Integer userId){
        if(addrInfoMapper.selectOne(Wrappers.<AddrInfo>lambdaQuery().eq(AddrInfo::getId,id).eq(AddrInfo::getUserId,userId)) != null){
            addrInfoMapper.deleteById(id);
            return Result.success();
        }
        return Result.error("-1","请求权限越界");
    }
}
