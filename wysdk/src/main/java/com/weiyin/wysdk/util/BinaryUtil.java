//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.weiyin.wysdk.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BinaryUtil {
    public BinaryUtil() {
    }

    public static String toBase64String(byte[] binaryData) {
        return new String(Base64.encode(binaryData, Base64.DEFAULT));
    }

    public static byte[] fromBase64String(String base64String) {
        return Base64.decode(base64String.getBytes(), Base64.DEFAULT);
    }

    public static byte[] calculateMd5(byte[] binaryData) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException("MD5 algorithm not found.");
        }

        messageDigest.update(binaryData);
        return messageDigest.digest();
    }

    public static byte[] calculateMd5(String filePath) throws IOException {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            FileInputStream is = new FileInputStream(new File(filePath));

            int lent;
            while ((lent = is.read(buffer)) != -1) {
                e.update(buffer, 0, lent);
            }

            is.close();
            return e.digest();
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
    }

    public static String calculateMd5Str(byte[] binaryData) {
        return getMd5StrFromBytes(calculateMd5(binaryData));
    }

    public static String calculateMd5Str(String filePath) throws IOException {
        return getMd5StrFromBytes(calculateMd5(filePath));
    }

    public static String calculateBase64Md5(byte[] binaryData) {
        return toBase64String(calculateMd5(binaryData));
    }

    public static String calculateBase64Md5(String filePath) throws IOException {
        return toBase64String(calculateMd5(filePath));
    }

    public static String getMd5StrFromBytes(byte[] md5bytes) {
        if (md5bytes == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < md5bytes.length; ++i) {
                sb.append(String.format("%02x", new Object[]{Byte.valueOf(md5bytes[i])}));
            }

            return sb.toString();
        }
    }
}
