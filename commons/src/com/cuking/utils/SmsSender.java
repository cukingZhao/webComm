package com.cuking.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * 发送短信的类,注入使用
 * 需要在springboot项目上面添加@EnableAsync 来开启异步发送
 * Created by xuchonggao on 2017/2/21.
 */
@Component
class SmsSender {

    private static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

//    private ExecutorService smsPool = Executors.newSingleThreadScheduledExecutor()

    private String smsSvcUrl = "http://43.243.130.33:8860";  //服务器URL 地址
    private String cust_code = "850060";                                   //账号
    private String password = "34K1PAZFMF";                                      //密码
    private String sp_code = "10690351010760055";              //接入码（扩展码）
    private int size = 100;

    @Async
    void sendSms(String mobiles, String content) {
        sendBatchSms(mobiles, content, sp_code, 0);
    }
    @Async
    void sendSms(String mobiles, String content, long task_id) {
        sendBatchSms(mobiles, content, sp_code, task_id);
    }
    @Async
    void sendSms(String mobiles, String content, String sp_code) {
        sendBatchSms(mobiles, content, sp_code, 0);
    }

    private void sendBatchSms(final String mobiles, final String content, final String sp_code, final long task_id) {
        String[] ms = mobiles.split(",");
        if (ms.length > 100) {
            List<String> mobilesLists = Arrays.asList(ms);
            int start = 0;
            int end = size;
            List<String> temp;
            while (end < mobilesLists.size() - 1) {
                temp = mobilesLists.subList(start, end);
                final String tempFinal = StringUtils.join(temp.toArray(), ",");
//                smsPool.submit(new Runnable() {
//                    @Override
//                    void run() {
                try {
                    doSend(tempFinal, content, sp_code, task_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    }
//                })
                start += size ;
                end += size;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //批量发送短信 防止触及上线
                }
            }
        } else {
//            smsPool.submit(new Runnable() {
//                @Override
//                void run() {
            try {
                doSend(mobiles, content, sp_code, task_id);
            } catch (IOException e) {
                logger.error("发送短信出现错误", e);
            }
//                }
//            })
        }
    }

    private void doSend(String mobiles, String content, String sp_code, long task_id) throws IOException {

        String urlencContent = URLEncoder.encode(content, "utf-8");
        String sign = sign(urlencContent, password, "utf-8");

        String postData = "content=${urlencContent}&destMobiles=${mobiles}&sign=${sign}&cust_code=${cust_code}&sp_code=${sp_code}&task_id=${task_id}" ;

        logger.info("发送短信:${content} to ${mobiles}");
        URL myurl = new URL(smsSvcUrl);
        URLConnection urlc = myurl.openConnection();
        urlc.setReadTimeout(1000 * 30);
        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        urlc.setAllowUserInteraction(false);
        DataOutputStream server = new DataOutputStream(urlc.getOutputStream());
        server.write(postData.getBytes("utf-8"));
        server.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "utf-8"));
        String resXml = "", s = "";
        while ((s = reader.readLine()) != null)
            resXml = resXml + s + "\r\n";
        reader.close();
        String resultMsg = URLDecoder.decode(resXml, "utf-8");
        String[] split = null;
        String codeMsg = null;
        if (StringUtils.isNotBlank(resultMsg) && (split = resultMsg.split(":")) != null && split.length > 0) {
            codeMsg = changeCodeToMsg(split[split.length - 1].trim());
        }
        String msg = resultMsg + "响应结果:" + codeMsg;
        logger.info("短信发送结果:{}", msg);
    }


    private static String changeCodeToMsg(String code) {
        switch (code) {
            case "0":
                return "发送成功" ;
            case "8":
                return "流量控制错，超出最高流量" ;
            case "12":
                return "用户账号未登录" ;
            case "15":
                return "通道不支持" ;
            case "22":
                return "客户账号已经被关闭" ;
            case "23":
                return "客户账号状态错误" ;
            case "24":
                return "客户账号余额不足" ;
            case "27":
                return "长短信拆分条数过多" ;
            case "29":
                return "错误号码" ;
            default:
                return "未知响应码" ;
        }

    }

    /**
     * 签名字符??
     * @param text 签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符?
     * @param text 签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错??指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
