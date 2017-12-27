package com.cuking.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gxcards.app.common.BusinessException;
import com.gxcards.app.config.GxConfig;
import com.gxcards.app.domain.enums.ComplaintState;
import com.gxcards.app.domain.enums.ResourceType;
import com.gxcards.app.domain.wxtemplate.MessageBean;
import com.gxcards.app.domain.wxtemplate.MiniProgramBean;
import com.gxcards.app.domain.wxtemplate.TemplateBean;
import com.gxcards.common.jf.MediaFile;
import com.gxcards.common.jf.utils.Charsets;
import com.gxcards.common.jf.utils.IOUtils;
import com.gxcards.common.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Component
public class WXUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    MyMailSender mailSender;
    @Autowired
    GxRedisLock gxRedisLock;

    private static Logger logger = LoggerFactory.getLogger(WXUtils.class);

    //redis 认证token
    private static String accessTokenGrantType = "client_credential";
    private static String openIdAndAccessTokenGrantType = "authorization_code";

    private static String H5_AUTHORIZEREDIRECT_URI = "";
    private static String appId = "";
    private static String appSecret = "";
    private static String authorizeRedirectUri = "";
    private static String WX_TEMPLATE_ID = "";

    private static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";         //获取普通的AccessToken的url
    private static String jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    //获取普通的AccessToken的url
    private static String codeAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //获取高权限的AccessToken的url
    private static String userinfoUrl = "https://api.weixin.qq.com/sns/userinfo";//获取高权限的AccessToken的url
    private static String authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";//获取高权限的AccessToken的url
    private static String getUrlMedia = "https://api.weixin.qq.com/cgi-bin/media/get";//获取高权限的AccessToken的url
    private static String templateMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public WXUtils(){}

    /**
     * 获取普通的AccessToken
     * @return
     */
    public String getAccessToken() {

        String s = stringRedisTemplate.opsForValue().get(GxConfig.ACCESS_TOKEN_REDIS_KEY);
        if (StringUtils.isNotBlank(s)) {
            return s;
        }
        logger.error("获取普通的AccessToken值为空");
        throw new BusinessException("获取普通的AccessToken值为空");


    }

    /**
     *  刷新AccessToken
     * @return
     */
    public String accessToken() {
        String accessToken = "";
        String lock = gxRedisLock.lock(GxConfig.ACCESS_TOKEN_REDIS_LOCK);
        try {
            StringBuffer sb = new StringBuffer(accessTokenUrl);
            sb.append("?grant_type=").append(accessTokenGrantType)
                    .append("&appid=").append(appId)
                    .append("&secret=").append(appSecret);

            //从新拉取授权
            String s = HttpUtils.get(sb.toString());
            JSONObject jsonObject = JSONObject.parseObject(s);

            if (null != jsonObject && null != jsonObject.get("access_token")) {
                accessToken = jsonObject.get("access_token") + "";
                //放到redis一份
                stringRedisTemplate.opsForValue().set(GxConfig.ACCESS_TOKEN_REDIS_KEY, accessToken, 7100L, TimeUnit.SECONDS);
            }

            logger.info("刷新accessToken成功!! accessToken:" + accessToken);
        } catch (Exception e) {
            //放到错误队列
            stringRedisTemplate.opsForValue().set(GxConfig.ACCESS_TOKEN_REDIS_ERROR_FLAG, DateUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            //todo 发送邮件
            mailSender.send(GxConfig.developMails, "刷新AccessToken异常", e.getMessage());

        } finally {
            gxRedisLock.unlock(GxConfig.ACCESS_TOKEN_REDIS_LOCK,lock);
        }

        return accessToken;
    }

    /**
     * 刷新jsapiTicket
     * @return
     */
    public String jsapiTicket() {
        String jsapiTicket = "";
        String lock = gxRedisLock.lock(GxConfig.JSAPI_TICKET_REDIS_LOCK);
        try {
            StringBuffer sb = new StringBuffer(jsapiTicketUrl);
            sb.append("?access_token=").append(getAccessToken())
                    .append("&type=").append("jsapi");

            //从新拉取授权
            String s = HttpUtils.get(sb.toString());
            JSONObject jsonObject = JSONObject.parseObject(s);

            if (null != jsonObject && null != jsonObject.get("ticket")) {
                jsapiTicket = jsonObject.get("ticket") + "";
                //放到redis一份
                stringRedisTemplate.opsForValue().set(GxConfig.JSAPI_TICKET_REDIS_KEY, jsapiTicket, 7000L, TimeUnit.SECONDS);
            }

            logger.info("刷新jsapiTicket成功!! jsapiTicket:" + jsapiTicket);
        } catch (Exception e) {
            //放到错误队列
            stringRedisTemplate.opsForValue().set(GxConfig.JSAPI_TICKET_REDIS_ERROR_FLAG, DateUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            //todo 发送邮件

            mailSender.send(GxConfig.developMails, "刷新jsapiTicket异常", e.getMessage());

        } finally {
            gxRedisLock.unlock(GxConfig.JSAPI_TICKET_REDIS_LOCK,lock);
        }

        return jsapiTicket;
    }

    /**
     * 获取普通的AccessToken
     * @return
     */
    public String getJsapiTicket() {
        String s = stringRedisTemplate.opsForValue().get(GxConfig.JSAPI_TICKET_REDIS_KEY);
        if (StringUtils.isNotBlank(s)) {
            return s;
        }
        logger.error("获取普通的AccessToken值为空");
        throw new BusinessException("获取普通的AccessToken值为空");

    }

    /**
     * 通过code换取网页授权access_token  注意这个code 使用点击确认登录时获得
     *
     * @param code
     */
    public Map<String, String> getOpenIdAndAccessToken(String code) {

        Map<String, String> map = new HashMap<>(8);

        StringBuffer sb = new StringBuffer(codeAccessTokenUrl);
        sb.append("?appid=").append(appId)
                .append("&secret=").append(appSecret)
                .append("&code=").append(code)
                .append("&grant_type=").append(openIdAndAccessTokenGrantType);

        String s = HttpUtils.get(sb.toString());
        JSONObject jsonObject = JSONObject.parseObject(s);

        if (null != jsonObject && null != jsonObject.get("openid")) {
            map.put("access_token", jsonObject.get("access_token") + "");
            map.put("expires_in", jsonObject.get("expires_in") + "");
            map.put("refresh_token", jsonObject.get("refresh_token") + "");
            map.put("openid", jsonObject.get("openid") + "");
            map.put("scope", jsonObject.get("scope") + "");

            return map;
        }

        //todo 微信授权失败
        logger.error("微信授权失败!  " + s);
        return new HashMap<>();

    }

    //log4j-over-slf4j.jar AND bound slf4j-log4j12.jar on the class path

    /**
     * 获取用户的信息
     * @param userAccessToken
     * @param openId
     * @return
     */
    public Map<String, String> userinfo(String userAccessToken, String openId) {

        Map<String, String> map = new HashMap<>(8);
        StringBuffer sb = new StringBuffer(userinfoUrl);
        sb.append("?access_token=").append(userAccessToken)
                .append("&openid=").append(openId)
                .append("&lang=").append("zh_CN");

        String s = HttpUtils.get(sb.toString());
        JSONObject jsonObject = JSONObject.parseObject(s);

        if (null != jsonObject && null != jsonObject.get("openid")) {
            map.put("openid", jsonObject.get("openid") + "");
            map.put("nickname", jsonObject.get("nickname") + "");
            map.put("sex", jsonObject.get("sex") + "");
            map.put("province", jsonObject.get("province") + "");
            map.put("city", jsonObject.get("city") + "");
            map.put("country", jsonObject.get("country") + "");
            map.put("headimgurl", jsonObject.get("headimgurl") + "");
            map.put("privilege", jsonObject.get("privilege") + "");
            map.put("unionid", jsonObject.get("unionid") + "");
            return map;
        }
        return new HashMap<>();
    }

    /**
     * 认证
     * @param response
     * @param callbackUrl
     * @throws IOException
     */
    public void authorizeRedirect(HttpServletResponse response, String callbackUrl) throws IOException {
        StringBuffer sb = new StringBuffer(authorizeUrl);
        sb.append("?appid=").append(appId)
                .append("&redirect_uri=").append(authorizeRedirectUri)
                .append("&response_type=").append("code")
                .append("&scope=").append("snsapi_userinfo")
                .append("&state=").append(callbackUrl)
                .append("#wechat_redirect");

        response.sendRedirect(sb.toString());
    }

    /**
     * 认证
     * @param response
     * @param callbackUrl
     * @throws IOException
     */
    public  void H5AauthorizeRedirect(HttpServletResponse response, String callbackUrl) throws IOException {
        StringBuffer sb = new StringBuffer(authorizeUrl);
        sb.append("?appid=").append(appId)
                .append("&redirect_uri=").append(H5_AUTHORIZEREDIRECT_URI)
                .append("&response_type=").append("code")
                .append("&scope=").append("snsapi_userinfo")
                .append("&state=").append(callbackUrl)
                .append("#wechat_redirect");
        response.sendRedirect(sb.toString());
    }

    /**
     * 微信服务器获取文件
     * @param mediaId
     * @return
     */
    public MediaFile getMediaFile(String mediaId) throws IOException {

        StringBuffer sb = new StringBuffer(getUrlMedia);
        sb.append("?access_token=").append(getAccessToken())
                .append("&media_id=").append(mediaId);

        MediaFile modia = download(sb.toString());
        if (StringUtils.isBlank(modia.getError())) {
            return modia;
        }

        throw new BusinessException("微信服务器获取文件失败! error:" + modia.getError());

    }

    public MediaFile download(String url) throws IOException {
        MediaFile mediaFile = new MediaFile();
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setConnectTimeout(25000);
        conn.setReadTimeout(25000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        String fullName;
        if (conn.getContentType().equalsIgnoreCase("text/plain")) {
            InputStream bis = conn.getInputStream();
            BufferedReader ds = new BufferedReader(new InputStreamReader(bis, Charsets.UTF_8));
            fullName = null;
            StringBuffer relName = new StringBuffer();

            while ((fullName = ds.readLine()) != null) {
                relName.append(fullName);
            }

            ds.close();
            IOUtils.closeQuietly(bis);
            mediaFile.setError(relName.toString());
        } else {
            BufferedInputStream bis1 = new BufferedInputStream(conn.getInputStream());
            String ds1 = conn.getHeaderField("Content-disposition");
            fullName = ds1.substring(ds1.indexOf("filename=\"") + 10, ds1.length() - 1);
            String relName1 = fullName.substring(0, fullName.lastIndexOf("."));
            String suffix = fullName.substring(relName1.length() + 1);
            mediaFile.setFullName(fullName);
            mediaFile.setFileName(relName1);
            mediaFile.setSuffix(suffix);
            mediaFile.setContentLength(conn.getHeaderField("Content-Length"));
            mediaFile.setContentType(conn.getHeaderField("Content-Type"));
            mediaFile.setFileStream(bis1);
        }

        return mediaFile;
    }

    /**
     * 微信发送模版消息
     *
     * @param flag 1-激活码, 2-会员账号
     * @param openId 接受着openid
     * @param productTitle 购买商品标题,如:爱奇艺激活码月卡\腾讯视频vip账号……
     * @param url 跳转h5链接 可为null
     *
     * @return
     */
    public  boolean sendBuySuccessTemplate(ResourceType resourceType, String openId, String productTitle,
                                           String
                                                   url) {

        // 商品信息
        String n = productTitle + "\n点击此消息可直接查看详情。\n";
        // swith
        switch (resourceType){
            case SALE_CARD:
                n = productTitle + "\n点击此消息可直接查看卡密。\n";
                break;
            case SHARE_COUNT:
                n = productTitle + "\n点击此消息可直接查看账号密码。\n";
                break;
        }

        // 备注remark
        String r = "备注信息";

        // 小程序配置
        MiniProgramBean miniProgramBean = new MiniProgramBean();
        miniProgramBean.setAppid("");
        miniProgramBean.setPagepath("");

        // 产品消息与备注配置
        Map<String, MessageBean> data = new HashMap<>();
        MessageBean name = new MessageBean();
        name.setValue(n);
        name.setColor("#173177");
        data.put("name", name);
        MessageBean remark = new MessageBean();
        remark.setValue(r);
        remark.setColor("#173177");
        data.put("remark", remark);

        // 主体消息配置
        TemplateBean templateBean = new TemplateBean();
        templateBean.setTouser(openId);
        templateBean.setTemplate_id(WX_TEMPLATE_ID);
        templateBean.setUrl(url);
        templateBean.setMiniprogram(miniProgramBean);
        templateBean.setData(data);

        // 发送字符串
        String param = JSON.toJSONString(templateBean);
        logger.debug("腾讯模版消息发送消息:{}", param);

        // 返回字符串
        String result = HttpUtils.post(templateMsgUrl + this.getAccessToken(), param);
        logger.info("腾讯模版消息返回消息{}", result);

        // 成功返回 {"errcode":0,"errmsg":"ok","msgid":540506108}
        if (result != null && result.contains("\"errcode\":0")) {
            return true;
        } else {
            logger.error("购买提醒模板消息发送异常!"+result);
            return false;
        }
    }


}
