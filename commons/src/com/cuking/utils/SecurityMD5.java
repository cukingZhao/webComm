package com.cuking.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pengpengrao on 16/10/19.
 */
public class SecurityMD5 {
    public static String encrypt(String inStr) {
        MessageDigest md = null;
        String out = "";
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(inStr.getBytes());
            out = byte2hex(digest);
            Hex.encodeHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();

    }

}
