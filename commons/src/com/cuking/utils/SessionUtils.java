//package com.cuking.utils;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by cuking on 2016/11/16.
// */
//public class SessionUtils {
//
//    static RedisTemplate redisTemplate = ApplicationUtils.getBean(RedisTemplate.class);
//
//    //过期时间
//    private Long expire = 1800l;
//
//    //session的id
//    private String sessionId = "RSESSIONID";
//
//    HttpServletRequest request;
//    HttpServletResponse response;
//
//
//    public SessionUtils(HttpServletRequest request, HttpServletResponse response) {
//        this.request = request;
//        this.response = response;
//    }
//
//    /**
//     * 获取cookie
//     *
//     * @param name
//     * @return
//     */
//    public Cookie getCookieByName(String name) {
//        Cookie[] cookies = request.getCookies();
//        if (ArrayUtils.isNotEmpty(cookies)) {
//            for (Cookie c : cookies) {
//                if (c.getName().equals(name)) {
//                    return c;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 初始化Session (有/更新过期时间,无/创建sessionKey)
//     */
//    public void initSession() {
//
//        String rsessionId = "";
//        Cookie cookie = getCookieByName(sessionId);
//        if (null == cookie) {
//            rsessionId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
//            cookie = new Cookie(sessionId, rsessionId);
//            cookie.setMaxAge(-1);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            //cookie.setDomain(".gxcards.com");
//        } else if (redisTemplate.hasKey(GxConfig.MES_SESSION_KEY + cookie.getValue())) {
//            //存在session 更新过期时间
//            redisTemplate.expire(GxConfig.MES_SESSION_KEY + rsessionId, expire, TimeUnit.SECONDS);
//        }
//    }
//
//
//    /**
//     * 获取Session
//     *
//     * @return
//     */
//    public Map<String, Object> getSession() {
//
//        Cookie cookie = getCookieByName(sessionId);
//        if (null == cookie) {
//            initSession();
//            return null;
//        }
//        String s = redisTemplate.opsForValue().get(GxConfig.MES_SESSION_KEY + cookie.getValue()) + "";
//        if (StringUtils.isNotBlank(s)) {
//            return JsonUtils.toObject(s, Map.class);
//        }
//        return null;
//    }
//
//    /**
//     * 获取Session
//     *
//     * @return
//     */
//    public <T> T getValueFromSession(String name, Class<T> clazz) {
//
//        Cookie cookie = getCookieByName(sessionId);
//        if (null == cookie) {
//            initSession();
//            return null;
//        }
//
//        String s = redisTemplate.opsForValue().get(GxConfig.MES_SESSION_KEY + cookie.getValue()) + "";
//        if (!StringUtils.isBlank(s)) {
//            Map m = JsonUtils.toObject(s, Map.class);
//            if(!CollectionUtils.isEmpty(m) && m.containsKey(name)){
//                return JsonUtils.toObject(JsonUtils.toJson(m.get(name)), clazz);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 设置session
//     *
//     * @param name
//     * @param value
//     */
//    public void setSession(String name, Object value) {
//        Cookie cookie = getCookieByName(sessionId);
//
//        if (null == cookie) {
//            initSession();
//            cookie = getCookieByName(sessionId);
//        }
//
//        Map<String, Object> map;
//        String s = redisTemplate.opsForValue().get(GxConfig.MES_SESSION_KEY + cookie.getValue()) + "";
//        if (StringUtils.isNotBlank(s)) {
//            //存在session
//            map = JsonUtils.toObject(s, Map.class);
//            if(null == map)map = new HashMap<>(1);
//            map.put(name, value);
//            redisTemplate.opsForValue().set(GxConfig.MES_SESSION_KEY + cookie.getValue(), JsonUtils.toJson(map), expire, TimeUnit.SECONDS);
//        } else {
//            //不存在session
//            map = new HashMap<>(1);
//            map.put(name, value);
//            redisTemplate.opsForValue().set(GxConfig.MES_SESSION_KEY + cookie.getValue(), JsonUtils.toJson(map), expire, TimeUnit.SECONDS);
//        }
//    }
//
//    /**
//     * 更新session
//     *
//     * @param map
//     */
//    public void updateSession(Map<String, Object> map) {
//        Cookie cookie = getCookieByName(sessionId);
//        if (null == cookie) {
//            initSession();
//            cookie = getCookieByName(sessionId);
//        }
//        redisTemplate.opsForValue().set(GxConfig.MES_SESSION_KEY + cookie.getValue(), JsonUtils.toJson(map), expire, TimeUnit.SECONDS);
//    }
//
//
//    /**
//     * 删除session中的指定值
//     *
//     * @param name
//     */
//    public void delSessionByName(String name) {
//        Cookie cookie = getCookieByName(sessionId);
//
//        if (null == cookie) {
//            initSession();
//            cookie = getCookieByName(sessionId);
//        }
//
//        Map<String, Object> map;
//        String s = redisTemplate.opsForValue().get(GxConfig.MES_SESSION_KEY + cookie.getValue()) + "";
//        if (StringUtils.isNotBlank(s)) {
//            map = JsonUtils.toObject(s, Map.class);
//            map.remove(name);
//            redisTemplate.opsForValue().set(GxConfig.MES_SESSION_KEY + cookie.getValue(), JsonUtils.toJson(map), expire, TimeUnit.SECONDS);
//        }
//    }
//
//    /**
//     * 删除session
//     */
//    public void delSession() {
//
//        Cookie cookie = getCookieByName(sessionId);
//        if (null == cookie) {
//            initSession();
//            cookie = getCookieByName(sessionId);
//        }
//        redisTemplate.delete(GxConfig.MES_SESSION_KEY + cookie.getValue());
//    }
//
//
//}
