package com.cuking.utils;

import com.gxcards.common.jf.kit.StrKit;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cuking on 2017/6/16.
 */
public class Sha1Utils {

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String packageSign(Map<String, String> params) {
        TreeMap sortedParams = new TreeMap(params);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Iterator var5 = sortedParams.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry param = (Map.Entry)var5.next();
            String value = (String)param.getValue();
            if(!StrKit.isBlank(value)) {
                if(first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                sb.append((String)param.getKey()).append("=");

                sb.append(value);
            }
        }
        return sb.toString();
    }

    public static String sign(Map map){
        return encode(packageSign(map));
    }

}
