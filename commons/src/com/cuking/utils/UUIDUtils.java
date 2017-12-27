package com.cuking.utils;

import com.gxcards.app.config.GxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis 的格式化主键生成器
 * Created by cuking on 2017/6/1.
 */
@Component
public class UUIDUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    Logger logger = LoggerFactory.getLogger(UUIDUtils.class);

    private int MINFULL = 5;        //最小位数

    private String spRedisUUIDPre = "SP_UUID:";        //自增值
    private String spRedisUUIDLockPre = "SP_UUID_LOCK:";        //自增值锁
    private String spOrderPre = "SPO";        //订单
    private String spOrderItemPre = "SPOI";        //订单项
    private String spUserPre = "SPU";         //用户
    private String spUserResourcePre = "SPUR";        //用户卡包
    private String spUserResourceItemPre = "SPURI";        //用户卡包
    private String spWithdrawalsLogPre = "SPUWWL";        //可提现金额日志
    private String spUserWalletLogPre = "SPUWL";        //钱包日志
    private String spRemittanceLogPre = "SPR";        //打款日志
    private String spResourcePre = "SPR";      //资源

    private String spComplainPre = "SPC";      //售后
    private String spComplainItemPre = "SPCI";      //售后项

    private String spComplainReplyPre = "SPCR";      //售后回复

    private String spMerchantResourcePre = "SPMR";      //卖家资源
    private String spActivateCodeStockLogPre = "SPACSL";      //卖家资源
    private String spResourceSkuPre = "SPSKU";      //sku
    private String spRefundPre = "SPTK";      //退款

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 订单
     *
     * @return
     */
    public String spOrderId() {
        return generateFormatId(spOrderPre);
    }

    /**
     * 订单项
     *
     * @return
     */
    public String spOrderItemId() {
        return generateFormatId(spOrderItemPre);
    }


    /**
     * 用户
     *
     * @return
     */
    public String spUserId() {
        return generateFormatId(spUserPre);
    }

    /**
     * 用户卡包
     *
     * @return
     */
    public String spUserResourceId() {
        return generateFormatId(spUserResourcePre);
    }

    /**
     * 用户卡包项
     *
     * @return
     */
    public String spUserResourceItemId() {
        return generateFormatId(spUserResourceItemPre);
    }


    /**
     * 钱包日志
     *
     * @return
     */
    public String spUserWalletLogId() {
        return generateFormatId(spUserWalletLogPre);
    }

    /**
     * 打款日志
     *
     * @return
     */
    public String spRemittanceLogId() {
        return generateFormatId(spRemittanceLogPre);
    }

    /**
     * 资源
     *
     * @return
     */
    public String spResourceId() {
        return generateFormatId(spResourcePre);
    }

    /**
     * 售后
     *
     * @return
     */
    public String spComplainId() {
        return generateFormatId(spComplainPre);
    }

    /**
     * 售后项
     *
     * @return
     */
    public String spComplainItemId() {
        return generateFormatId(spComplainItemPre);
    }


    /**
     * 售后回复
     *
     * @return
     */
    public String spComplainReplyId() {
        return generateFormatId(spComplainReplyPre);
    }

    /**
     * 商户资源
     *
     * @return
     */
    public String spMerchantResourceId() {
        return generateFormatId(spMerchantResourcePre);
    }

    /**
     * 入库日志
     *
     * @return
     */
    public String spActivateCodeStockLogId() {
        return generateFormatId(spActivateCodeStockLogPre);
    }

    /**
     * sku
     *
     * @return
     */
    public String spResourceSkuId() {
        return generateFormatId(spResourceSkuPre);
    }

    /**
     * 退款
     *
     * @return
     */
    public String spRefundId() {
        return generateFormatId(spRefundPre);
    }

    /**
     * 可提现金额日志
     *
     * @return
     */
    public String spWithdrawalsLogId() {
        return generateFormatId(spWithdrawalsLogPre);
    }

    public String generateFormatId(String pre) {
        Long val = stringRedisTemplate.opsForValue().increment(spRedisUUIDPre + pre, 1L);
        //设置生命周期
        if (1L == val.longValue()) {
            stringRedisTemplate.expire(spRedisUUIDPre + pre, DateUtil.getTodayDieSecond(), TimeUnit.SECONDS);
        }
        StringBuffer sb = new StringBuffer(pre);
        sb.append(DateUtil.getSimpleDate(new Date()));
        sb.append(GxConfig.SP_ID_PRE).append(coverZero(MINFULL, Integer.parseInt(val + "")));
        return sb.toString();
    }


    public static String coverZero(int full, Integer val) {
        int length = String.valueOf(val).length();
        StringBuffer sb = new StringBuffer();
        while (length < full) {
            sb.append("0");
            length++;
        }
        sb.append(val);
        return sb.toString();
    }


    public static void main(String[] args) {
        long l = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
//            String.format("%05d",12);
//           System.err.println(coverZero2(4, i));
        }
        System.err.println((System.nanoTime()-l)/1000000);
    }
}
