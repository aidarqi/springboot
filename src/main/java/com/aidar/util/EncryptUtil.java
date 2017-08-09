package com.aidar.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright (C), 2017, spring boot 自我学习
 * @desc 加密工具类，包含MD5，SHA1加密
 * @date 2017-04-19 14:08:51
 */
public class EncryptUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    public static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    public static String ALGORITHM_SHA1 = "SHA-1";

    public static byte[] sha1ToByte(String data, Charset charset) {
        byte[] result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_SHA1);
            result = ecrypt(messageDigest, data, charset);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info(e.getMessage(), e);
        }
        return result;
    }

    public static String sha1ToString(String data, Charset charset) {
        byte[] result = sha1ToByte(data, charset);
        return bytesToString(result);
    }

    private static byte[] ecrypt(MessageDigest messageDigest, String data, Charset charset) {
        byte[] result = null;
        if (null != data && data.length() > 0) {
            try {
                byte[] bytes = data.getBytes(charset);
                messageDigest.update(bytes);
                result = messageDigest.digest();
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
        return result;
    }

    private static String bytesToString(byte[] bytes) {
        String result = null;
        if (null != bytes && bytes.length > 0) {
            int len = bytes.length;
            char[] myChar = new char[len * 2];
            int k = 0;
            for (byte b : bytes) {
                myChar[k++] = HEX_DIGITS[b >>> 4 & 0x0f];
                myChar[k++] = HEX_DIGITS[b & 0x0f];
            }
            result = new String(myChar);
        }
        return result;
    }

    public static String MD5(String plainText) {
        String result = null;
        // 首先判断是否为空
        if (StringUtils.isBlank(plainText)) {
            return null;
        }
        try {
            // 首先进行实例化和初始化
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 得到一个操作系统默认的字节编码格式的字节数组
            byte[] btInput = plainText.getBytes();
            // 对得到的字节数组进行处理
            md.update(btInput);
            // 进行哈希计算并返回结果
            byte[] btResult = md.digest();
            // 进行哈希计算后得到的数据的长度
            StringBuffer sb = new StringBuffer();
            for (byte b : btResult) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
