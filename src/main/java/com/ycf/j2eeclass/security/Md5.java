package com.ycf.j2eeclass.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Md5 {
    /**利用MD5进行加密*/
    public static String EncoderByMd5(String str) /*throws NoSuchAlgorithmException, UnsupportedEncodingException*/{
        try{
            //确定计算方法
            MessageDigest md5=MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            return base64en.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;

    }

    /**判断用户密码是否正确
     *newpasswd 用户输入的密码
     *oldpasswd 正确密码*/
    public static boolean checkPassword(String newPwd,String oldPwd) /*throws NoSuchAlgorithmException, UnsupportedEncodingException*/{
        return EncoderByMd5(newPwd).equals(oldPwd);
    }
}