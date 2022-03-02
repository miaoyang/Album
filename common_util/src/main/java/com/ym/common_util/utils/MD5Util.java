package com.ym.common_util.utils;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 19:37
 */
public class MD5Util {
    public static String getMD5(@NotNull String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();
        return getString(m);
    }
    private static String getString(byte[] b) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0,length=b.length; i < length; i++) {
            int a = b[i];
            if (a < 0)
                a += 256;
            if (a < 16)
                buf.append("0");
            buf.append(Integer.toHexString(a));

        }
        return buf.toString();  //32位
    }
}
