package com.jd.smartcloudmobilesdk.demo.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

        MessageDigest msgDigest;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        try {
            msgDigest.update(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        return new String(encodeHex(bytes));
    }

    private static char[] encodeHex(byte[] data) {
        if (data == null) {
            return new char[0];
        }

        int length = data.length;
        char[] out = new char[length << 1];

        for (int i = 0, j = 0; i < length; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

}