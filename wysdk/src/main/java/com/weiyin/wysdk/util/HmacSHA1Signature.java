
package com.weiyin.wysdk.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA1Signature {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String ALGORITHM = "HmacSHA1";
    private static final String VERSION = "1";
    private static Mac macInstance;

    public String getAlgorithm() {
        return ALGORITHM;
    }

    public String getVersion() {
        return VERSION;
    }

    public String signature(String key, String data) {
        try {
            byte[] ex = this.sign(key.getBytes(DEFAULT_ENCODING), data.getBytes(DEFAULT_ENCODING));
            return BinaryUtil.toBase64String(ex);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("Unsupported algorithm: UTF-8");
        }
    }

    private byte[] sign(byte[] key, byte[] data) {
        try {
            if (macInstance == null) {
                synchronized (HmacSHA1Signature.class) {
                    if (macInstance == null) {
                        macInstance = Mac.getInstance(ALGORITHM);
                    }
                }
            }

            Mac ex1;
            try {
                ex1 = (Mac) macInstance.clone();
            } catch (CloneNotSupportedException var5) {
                ex1 = Mac.getInstance(ALGORITHM);
            }

            ex1.init(new SecretKeySpec(key, ALGORITHM));
            return ex1.doFinal(data);
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Unsupported algorithm: HmacSHA1");
        } catch (InvalidKeyException var8) {
            throw new RuntimeException();
        }
    }
}
