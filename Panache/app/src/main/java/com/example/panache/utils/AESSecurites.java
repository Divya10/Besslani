/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.util.Base64;


import com.example.panache.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESSecurites {
    private String SecretKey = BuildConfig.SECRET_KEY;
    private String IV = BuildConfig.IV;

    private static AESSecurites instance = null;

    public static AESSecurites getInstance() {

        if (instance == null) {
            instance = new AESSecurites();
        }
        return instance;
    }

    public String encrypt(String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        byte[] srcBuff = message.getBytes("UTF8");
        //here using substring because AES takes only 16 or 24 or 32 byte of key
        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec(IV.substring(0,16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] dstBuff = ecipher.doFinal(srcBuff);
        String base64 = Base64.encodeToString(dstBuff, Base64.DEFAULT);
        return base64;
    }

    public String decrypt(String encrypted) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {

        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec(IV.substring(0,16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] originalBytes = ecipher.doFinal(raw);
        String original = new String(originalBytes, "UTF8");
        return original;
    }

}
