package com.ycf.j2eeclass.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycf.j2eeclass.config.Result;
import com.ycf.j2eeclass.entity.User;
import com.ycf.j2eeclass.mapper.UserMapper;
import com.ycf.j2eeclass.security.Md5;
import com.ycf.j2eeclass.security.TokenUtil;
import com.ycf.j2eeclass.util.JudgeEmpty;
import com.ycf.j2eeclass.util.TrimAll;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

//    /**
//     * 在数据库里通过用户名找Id，找不到返回-1,如果为""返回-2
//     * @param username
//     * @return
//     */
//    public Integer findIdByUsername(String username){
//        if(StrUtil.isEmpty(username)){
//            return -2;
//        }
//        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
//        if(res!=null){
//            return res.getId();
//        }
//        return -1;
//    }
//
//    /**
//     * 在数据库里通过Id找用户名，找不到返回null
//     * @param id
//     * @return
//     */
//    public String findUsernameById(Integer id){
//        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,id));
//        if(res!=null){
//            return res.getUsername();
//        }
//        return null;
//    }

    /**
     * 通过id查找用户信息
     * @param id
     * @return
     */
    public Result findUserById(Integer id){
        User user = userMapper.selectById(id);
        if(user!=null){
            return Result.success(user);
        }
        return Result.error("-1","用户信息访问出错");
    }

    /**
     * 通过id更新用户信息
     * @param user
     * @return
     */
    public Result updateUserInfo(User user){
        TrimAll.trim(User.class,user); //去掉首位空格
        String msg = JudgeEmpty.isBlank(User.class,user); //利用反射判断是否有元素为null
        if(!msg.equals("ok")){
            return Result.error("-1",msg);
        }
        userMapper.updateById(user);
        return Result.success();
    }

    /**
     * 注册时向数据库中添加用户，用户名不能重复
     * @param user
     */
    public Result addUser(User user){
        TrimAll.trim(User.class,user);
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        //数据库要保证只能查询到一条数据，selectOne，需要注册时验证,用户名不能为重复
        if(res!=null){
            return Result.error("-1","用户名重复");
        }
        String msg = JudgeEmpty.isBlank(User.class,user); //利用反射判断是否有元素为null
        if(!msg.equals("ok")){
            return Result.error("-1",msg);
        }
        if(!(user.getRole().equals("user") || user.getRole().equals("postman"))){
            return Result.error("-1","错误的身份注册");
        }
        user.setPassword(Md5.EncoderByMd5(user.getPassword())); //密码加密编码
        userMapper.insert(user);
        return Result.success();
    }

    /**
     * 登录时校验身份
     * @param user
     * @return
     */
    public Result checkUser(User user){
        TrimAll.trim(User.class,user);
        user.setPassword(Md5.EncoderByMd5(user.getPassword())); //哈希加密
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(res==null){
            return Result.error("-1","用户名或密码错");
        }
        String jwtToken = TokenUtil.createJwtStr(res.getUsername(), String.valueOf(res.getId()),res.getRole());  //登录返回token
        return Result.success(jwtToken);
    }

    /**
     * 检测该普通用户是否存在
     * @param username
     * @return
     */
    public Result isUserExist(String username){
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username).eq(User::getRole,"user"));
        if (user != null){
            return Result.success();
        }
        return Result.error("-2","此用户名不存在或此用户不为普通用户");
    }

    /**
     * 找到所有的员工或者快递员
     * @param pageNum
     * @param pageSize
     * @param search
     * @param role
     * @return
     */
    public Result findAllUser(Integer pageNum, Integer pageSize, String search, String role){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().eq(User::getRole, role);
        if(!(StrUtil.isEmpty(search))){
            wrapper.like(User::getUsername,search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize)/*分页对象*/, wrapper);
        return Result.success(userPage);
    }

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    public Result deleteUser(Long id){
        userMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 更新密码
     * @param oldPwd
     * @param newPwd
     * @param id
     * @return
     */
    public Result updatePwd(String oldPwd, String newPwd, String conPwd, Integer id){
        if(!newPwd.equals(conPwd)){
            return Result.error("-1","两次密码不一致，请检查");
        }

        User user = userMapper.selectById(id);
        if(user==null){
            return Result.error("-1","id不存在");
        }
        String pwd = user.getPassword();
        if (Md5.checkPassword(oldPwd,pwd)){
            user.setPassword(Md5.EncoderByMd5(newPwd));
            userMapper.updateById(user);
            return Result.success();
        }
        return  Result.error("-1","密码错误");
    }
}
