package com.ycf.j2eeclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String role;
    private String username;
    private String password;
    private String sex;
    private Integer age;
}
