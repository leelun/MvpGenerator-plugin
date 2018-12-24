package com.common.core.utils;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author liulun
 * @version V1.0
 * @Description: des3 加密解密
 * @date 2016/12/5 10:40
 */
public class Des3Util {
    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private final static String encoding = "utf-8";
    public static String decrypt(String key,String keyiv,String encryptStr) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(encoding));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(keyiv.getBytes(encoding));
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
        return new String(cipher.doFinal(Base64.decode(encryptStr,Base64.DEFAULT)), encoding);
    }

    public static String encrypt(String key,String keyiv,String plainText) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(encoding));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(keyiv.getBytes(encoding));
        cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
        return Base64.encodeToString(cipher.doFinal(plainText.getBytes(encoding)),Base64.DEFAULT);
    }

    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);
        return keygen.generateKey().getEncoded();
    }
}
