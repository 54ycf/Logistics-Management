package com.ycf.j2eeclass.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单编码码生成器，生成32位数字编码，
 * @生成规则 17位时间戳+8位(用户id加密&随机数)
 * Date:2017年9月8日上午10:05:19
 * @author jiwengjian
 */
public class OrderCodeFactory {

    /** 随即编码 */
    private static final int[] r = new int[]{7, 9, 6, 2, 8, 1, 3, 0, 5, 4};
    /** 用户id和随机数总长度 */
    private static final int maxLength = 8;

    /**
     * 更具id进行加密+加随机数组成固定长度编码
     */
    private static String toCode(Integer id) {
        String idStr = id.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1 ; i >= 0; i--) {
            idsbs.append(r[idStr.charAt(i)-'0']);
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }

    /**
     * 生成固定长度随机码
     * @param n    长度
     */
    private static long getRandom(long n) {
        long min = 1,max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
        return rangeLong;
    }

    /**
     * 生成不带类别标头的编码
     * @param userId
     */
    private static synchronized String getCode(Integer userId, Date date){
        userId = userId == null ? 10000 : userId;
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date) + toCode(userId);
    }

    /**
     * 生成订单单号编码
     * @param userId
     */
    public static String getOrderCode(Integer userId, Date date){
        return getCode(userId, date);
    }

}