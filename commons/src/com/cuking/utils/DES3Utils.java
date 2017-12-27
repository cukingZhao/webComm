package com.cuking.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * 卡密 加密算法
 * 3DES 加密
 */
public class DES3Utils {

    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

    public static final byte[] DEFAULT_IV = "19283746".getBytes();


    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
        return cipher.doFinal(data);
    }
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
        return cipher.doFinal(data);
    }

    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);
        return keygen.generateKey().getEncoded();
    }

    public static byte[] randomIVBytes() {
        Random ran = new Random();
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) ran.nextInt(Byte.MAX_VALUE + 1);
        }
        return bytes;
    }




    public static String decrypt(String data,String key) throws Exception {
        return new String(decrypt(Base64.decodeBase64(data),key.getBytes(),DEFAULT_IV));
    }



    public static String encrypt(String data,String key) throws Exception {
        return Base64.encodeBase64String(encrypt(data.getBytes(),key.getBytes(),DEFAULT_IV));
    }
}