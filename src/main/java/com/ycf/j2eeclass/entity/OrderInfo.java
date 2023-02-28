package com.ycf.j2eeclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("orderinfo")
@Data
public class OrderInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String orderNum;

    private String senderName;
    private String sendPlace;
    private Date sendTime;
    private String senderPhone;

    private String receiverName;
    private String receivePlace;
    private Date receiveTime;
    private String receiverPhone;

    private String postmanName;

    private BigDecimal packageWeight;
    private BigDecimal packagePrice;
    private String packageState;
    private String packageType;
}
