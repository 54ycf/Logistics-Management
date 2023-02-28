package com.ycf.j2eeclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("addrinfo")
@Data
public class AddrInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private Integer status;
    private String contacts;
    private String phoneNum;
    private String addr;
}
