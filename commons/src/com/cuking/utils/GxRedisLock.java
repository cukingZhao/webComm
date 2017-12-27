package com.cuking.utils;

import com.gxcards.app.common.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @version 1.5
 * @description: redis lock 1.5
 * @author: cuking
 * @date: 2017/11/7
 */
@Component
public class GxRedisLock {

    Logger logger = LoggerFactory.getLogger(GxRedisLock.class);

    /**
     * redis 的过期时间 单位秒
     */
    public static final int EXPIRE = 31;

    /**
     *
     */
    private static final long TIME_OUT = 30000;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;


    /**
     * 加锁
     * <p>
     * todo 自旋检验 cpu 性能浪费很严重 推荐算法 redLock
     *
     * @param key     锁的name
     * @param val     锁的唯一标识
     * @param timeout 超时时间 毫秒
     */
    public String lock(String key, String val, long timeout) {

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeout) {
            if (System.currentTimeMillis() - start > 10000) {
                logger.error("加锁超过10S key:" + key);
                if(-1 == stringRedisTemplate.getExpire(key).intValue()){
                    logger.error("死锁已自动修复:" + key);
                }
            }

            //判断是否为锁定的资源
            if (stringRedisTemplate.opsForValue().setIfAbsent(key, val)) {
                //注意 Redis server 在此处挂掉 就死锁了
                //不存在锁,新建锁  防止死锁 设置生命周期
                stringRedisTemplate.expire(key, EXPIRE, TimeUnit.SECONDS);
                logger.info("加锁成功 key:{},val:{}", new Object[]{key, val});
                return val;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new BusinessException("系统繁忙请重试");
            }
        }
        throw new BusinessException("系统繁忙请重试");
    }

    /**
     * 加锁
     *
     * @param key 锁的唯一标识
     */
    public String lock(String key) {
        return lock(key, snowflakeIdWorker.nextId(), TIME_OUT);
    }

    /**
     * 解锁
     *
     * @param key 锁的名称
     * @param val 锁的唯一值
     */
    public void unlock(String key, String val) {

        if(null == val){
            return;
        }

        if (stringRedisTemplate.hasKey(key) && val.equals(stringRedisTemplate.opsForValue().get(key))) {
            stringRedisTemplate.delete(key);
            logger.info("解锁成功 key:{},val:{}", new Object[]{key, val});
        }
    }


}
