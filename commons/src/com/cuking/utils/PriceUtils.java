package com.cuking.utils;

import java.math.BigDecimal;

/**
 * Created by xuchonggao on 2016/9/22.
 */
public class PriceUtils {

    public static final BigDecimal b100 = new BigDecimal(100);
    public static final BigDecimal B_MINUS_1 = new BigDecimal(-1);

    /**
     * 将分转成元 两位小数 进一位
     * @param para
     * @return
     */
    public static BigDecimal point2Yuan(BigDecimal para){
        return para.divide(b100,2,BigDecimal.ROUND_CEILING);
    }


    /**
     * 将元转成分
     * @param para
     * @return
     */
    public static BigDecimal yuan2Point(BigDecimal para){
        return para.multiply(b100);
    }


    /**
     *  流水回扣 单位是分
     * @param amount
     * @param rate
     * @return  最终得钱
     */
    public static BigDecimal calcuRebate(BigDecimal amount, BigDecimal rate){
        BigDecimal bigDecimal = amount.multiply(rate).setScale(0, BigDecimal.ROUND_CEILING);
        return amount.subtract(bigDecimal).setScale(0, BigDecimal.ROUND_CEILING);
    }

    /**
     * 计算天价 单位是分
     * @param amount
     * @param days
     * @return
     */
    public static BigDecimal calcuDayPrice(BigDecimal amount, BigDecimal days){
        return amount.divide(days,0,BigDecimal.ROUND_CEILING);
    }

    /**
     * 计算天价 单位是分
     * @param amount
     * @param days
     * @return
     */
    public static BigDecimal calcuRealPrice(BigDecimal amount, BigDecimal days, BigDecimal allDays){
        return days.divide(allDays,3,BigDecimal.ROUND_CEILING).multiply(amount).setScale(0, BigDecimal.ROUND_CEILING);

    }


    /**
     * 判断传输过来的金额是否只有二位小数点
     * @param bigDecimal
     * @return
     */
    public static boolean isNumber(BigDecimal bigDecimal){
        int i = 2;
        if(new BigDecimal(bigDecimal.intValue()).compareTo(bigDecimal) == 0){
            return true;
        }
        if(bigDecimal.toString().split("\\.")[1].length() <= i){
            return true;
        }else{
            return false;
        }
    }


    public static void main(String[] args) {
        boolean number = isNumber(new BigDecimal("12.11"));


        System.out.println(number);

//        BigDecimal bigDecimal = calcuDayPrice(new BigDecimal("100"), new BigDecimal("3"));
//
//        BigDecimal bigDecimal1 = calcuRebate(new BigDecimal("100"), new BigDecimal("0.2"));
//
//        BigDecimal bigDecimal2 = calcuRebate(new BigDecimal(2), new BigDecimal(0.2));
//
//        BigDecimal bigDecimal3 = calcuRealPrice(new BigDecimal("100"), new BigDecimal(1), new BigDecimal(3));
//
//        int i =1;

    }

}
