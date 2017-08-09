package com.aidar.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Copyright (C), 2017, spring boot 自我学习
 * @desc RSA util相关
 * @date 2017-05-16 10:51:22
 */
@Component
public class RSAUtil {

    private static Logger LOGGER = Logger.getLogger(RSAUtil.class);
    private static String KEY_ALGORITHM = "RSA";

    public static final int KEY_SIZE = 2048; // 密钥长度, 一般2048
    public static final int RESERVE_BYTES = 11;

    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密block需要预留11字节
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";// sign值生成方式
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private String algorithm;// 密钥生成模式
    private String signature;// 签名sign生成模式
    private Charset charset;// 编码格式

    private int keySize;// RSA密钥长度必须是64的倍数，在512~65536之间
    private int decryptBlock; // 默认keySize=2048的情况下, 256 bytes
    private int encryptBlock; // 默认keySize=2048的情况下, 245 bytes

    private static KeyFactory keyFactory;
    static {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("生成RSA密钥对异常，" + e);
        }
    }

    public RSAUtil() {
        this(CIPHER_ALGORITHM);
    }

    public RSAUtil(String algorithm) {
        this(algorithm, CHARSET_UTF8);
    }

    public RSAUtil(int keySize) {
        this(CIPHER_ALGORITHM, keySize, CHARSET_UTF8, SIGNATURE_ALGORITHM);
    }

    public RSAUtil(String algorithm, Charset charset) {
        this(algorithm, KEY_SIZE, charset, SIGNATURE_ALGORITHM);
    }

    public RSAUtil(String algorithm, int keySize, Charset charset, String signature) {
        this.algorithm = algorithm;
        this.signature = signature;
        this.charset = charset;
        this.keySize = keySize;

        this.decryptBlock = this.keySize / 8;
        this.encryptBlock = decryptBlock - RESERVE_BYTES;
        System.out.println("algorithm: " + algorithm + ";signature: " + signature + ";charset: " + charset + ";keySize: " + keySize
            + ";decryptBlock: " + decryptBlock + ";encryptBlock: " + encryptBlock );
    }

    /**
     * 公钥加密
     * @param publicKeyStr 公钥字符串
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encrypt(String publicKeyStr, String data) {
        PublicKey publicKey = restorePublicKey(publicKeyStr);
        return encrypt(publicKey, data);
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encrypt(PublicKey publicKey, String data) {
        byte[] bytes = data.getBytes(charset);
        return encrypt(publicKey, bytes);
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encrypt(PublicKey publicKey, byte[] data) {
        byte[] encrypt = null;

        int dataLen = data.length;
        // 计算分段加密的block数 (向上取整)
        int nBlock = (dataLen / encryptBlock);
        if ((dataLen % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);

        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 分段加密
            for (int offset = 0; offset < dataLen; offset += encryptBlock) {
                // block大小: encryptBlock 或 剩余字节数
                int inputLen = (dataLen - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }

                // 得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(encryptedBlock);
            }

            outbuf.flush();
            encrypt = outbuf.toByteArray();
            outbuf.close();
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            LOGGER.info("使用RSA，加密数据异常", e);
        }

        byte[] enData = Base64.encodeBase64(encrypt);
        return StringUtils.newString(enData, charset.name());
    }

    /**
     * 公钥解密
     * @param publicKeyStr 公钥字符串
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(String publicKeyStr, String data) throws Exception {
        PublicKey publicKey = restorePublicKey(publicKeyStr);
        return decryptByPublicKey(publicKey, data);
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(PublicKey publicKey, String data) throws Exception {
        byte[] bytes = Base64.decodeBase64(data.getBytes(charset));
        return decryptByPublicKey(publicKey, bytes);
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(PublicKey publicKey, byte[] data) throws Exception {
        byte[] decrypt = null;

        int dataLen = data.length;
        // 计算分段解密的block数 (理论上应该能整除)
        int nBlock = (dataLen / decryptBlock);
        if ((dataLen % decryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个encryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);

        Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 分段解密
        for (int offset = 0; offset < data.length; offset += decryptBlock) {
            // block大小: decryptBlock 或 剩余字节数
            int inputLen = (data.length - offset);
            if (inputLen > decryptBlock) {
                inputLen = decryptBlock;
            }

            // 得到分段解密结果
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            // 追加结果到输出buffer中
            outbuf.write(decryptedBlock);
        }
        outbuf.flush();
        decrypt = outbuf.toByteArray();
        outbuf.close();

        return StringUtils.newString(decrypt, charset.name());
    }

    /**
     * 私钥加密
     * @param privateKeyStr 私钥字符串
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(String privateKeyStr, String data) {
        PrivateKey privateKey = restorePrivateKey(privateKeyStr);
        return encryptByPrivateKey(privateKey, data);
    }

    /**
     * 私钥加密
     * @param privateKey 私钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(PrivateKey privateKey, String data) {
        byte[] bytes = data.getBytes(charset);
        return encryptByPrivateKey(privateKey, bytes);
    }

    /**
     * 私钥加密
     * @param privateKey 私钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
        byte[] encrypt = null;

        int dataLen = data.length;
        // 计算分段加密的block数 (向上取整)
        int nBlock = (dataLen / encryptBlock);
        if ((dataLen % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);

        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // 分段加密
            for (int offset = 0; offset < dataLen; offset += encryptBlock) {
                // block大小: encryptBlock 或 剩余字节数
                int inputLen = (dataLen - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }

                // 得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(encryptedBlock);
            }

            outbuf.flush();
            encrypt = outbuf.toByteArray();
            outbuf.close();
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            LOGGER.info("使用RSA，加密数据异常", e);
        }

        byte[] enData = Base64.encodeBase64(encrypt);
        return StringUtils.newString(enData, charset.name());
    }

    /**
     * 私钥解密
     * @param privateKeyStr 私钥字符串
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decrypt(String privateKeyStr, String data) throws Exception {
        PrivateKey privateKey = restorePrivateKey(privateKeyStr);
        return decrypt(privateKey, data);
    }

    /**
     * 私钥解密
     * @param privateKey 私钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decrypt(PrivateKey privateKey, String data) throws Exception {
        byte[] bytes = Base64.decodeBase64(data.getBytes(charset));
        return decrypt(privateKey, bytes);
    }

    /**
     * 私钥解密
     * @param privateKey 私钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        byte[] decrypt = null;

        int dataLen = data.length;
        // 计算分段解密的block数 (理论上应该能整除)
        int nBlock = (dataLen / encryptBlock);
        if ((dataLen % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个encryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);

        Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 分段解密
        for (int offset = 0; offset < data.length; offset += decryptBlock) {
            // block大小: decryptBlock 或 剩余字节数
            int inputLen = (data.length - offset);
            if (inputLen > decryptBlock) {
                inputLen = decryptBlock;
            }

            // 得到分段解密结果
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            // 追加结果到输出buffer中
            outbuf.write(decryptedBlock);
        }
        outbuf.flush();
        decrypt = outbuf.toByteArray();
        outbuf.close();

        return StringUtils.newString(decrypt, charset.name());
    }

    /**
     * 根据keyFactory生成Cipher
     * @return cipher
     */
    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOGGER.info("生成RSA Cipher异常", e);
        }
        return cipher;
    }

    /**
     * 公钥还原，将公钥转化为PublicKey对象,X509EncodedKeySpec 用于构建公钥的规范
     * @param publicKeyStr 公钥字符串
     * @return PublicKey对象
     */
    public PublicKey restorePublicKey(String publicKeyStr) {
        PublicKey publicKey = null;

        byte[] keyBytes = Base64.decodeBase64(publicKeyStr.getBytes(charset));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            publicKey = keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.info("公钥还原，将公钥转化为PublicKey对象异常", e);
        }

        return publicKey;
    }

    /**
     * 私钥还原，将私钥转化为privateKey对象，PKCS8EncodedKeySpec 用于构建私钥的规范
     * @param privateKeyStr 私钥字符串
     * @return PrivateKey对象
     */
    public PrivateKey restorePrivateKey(String privateKeyStr) {
        PrivateKey privateKey = null;

        byte[] keyBytes = Base64.decodeBase64(privateKeyStr.getBytes(charset));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.info("私钥还原，将私钥转化为privateKey对象异常", e);
        }

        return privateKey;
    }

    /**
     * 生成密钥对
     * @param keySize 密钥长度
     * @return 密钥对
     */
    public static KeyPair generateKeyPair(Integer keySize) {
        KeyPair keyPair = null;
        if (null == keySize) {
            keySize = KEY_SIZE;
        }
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(keySize);

            keyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("RSA公私钥对生成异常：", e);
        }
        return keyPair;
    }

    /**
     * RSA生成sign值
     * @param privateKeyStr 私钥字符串
     * @param data 数据
     * @return sign值
     * @throws Exception 异常
     */
    public String generateSign(String privateKeyStr, String data) throws Exception {
        return generateSign(restorePrivateKey(privateKeyStr), data);
    }

    /**
     * RSA生成sign值
     * @param privateKey 私钥
     * @param data 数据
     * @return sign值
     * @throws Exception 异常
     */
    public String generateSign(PrivateKey privateKey, String data) throws Exception {
        Signature signature = Signature.getInstance(this.signature);
        signature.initSign(privateKey);
        signature.update(data.getBytes(charset));
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * RSA验签
     * @param publicKeyStr 公钥字符串
     * @param data 数据
     * @param sign sign值
     * @return 验签是否成功
     * @throws Exception 异常
     */
    public boolean verifyRSA(String publicKeyStr, String data, String sign) throws Exception {
        return verifyRSA(restorePublicKey(publicKeyStr), data, sign);
    }

    /**
     * RSA验签
     * @param publicKey 公钥
     * @param data 数据
     * @param sign sign值
     * @return 验签是否成功
     * @throws Exception 异常
     */
    public boolean verifyRSA(PublicKey publicKey, String data, String sign) throws Exception {
        Signature signature = Signature.getInstance(this.signature);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(charset));

        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 生成私钥 base64加密
     * @param key 密钥
     * @param charset 编码格式
     * @return 私钥
     */
    public static String convertToString(Key key, Charset charset) {
        byte[] keyBytes = key.getEncoded();
        return StringUtils.newString(Base64.encodeBase64(keyBytes), charset.name());
    }

    public int getKeySize() {
        return keySize;
    }

    public Charset getCharset() {
        return charset;
    }

    public static void main(String[] args) throws Exception {
        RSAUtil rsaUtil = new RSAUtil();
        KeyPair pair = RSAUtil.generateKeyPair(null);
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
        String publicKeyStr = RSAUtil.convertToString(publicKey, rsaUtil.getCharset());
        String privateKeyStr = RSAUtil.convertToString(privateKey, rsaUtil.getCharset());

        System.out.println(publicKeyStr.length() + "位公钥：" + publicKeyStr);
        System.out.println(privateKeyStr.length() + "位私钥：" + privateKeyStr);

        String data = "页 头条 突发 新民眼 时政 社会 民生 科教 健康 法谭 财经 文娱圈 运动汇 环球 军事 夜光杯 国艺 国内 新媒体小灶 侬好上海 装潢 读晚报展开东电首次测得福岛核电站安全壳底部污水辐射量来源：中国新闻网   2017-03-20 09:30　　中新网3月20日电 据日本共同社报道，日本东京电力公司本月19日宣布，18日通过自动行走机器人对福岛第一核电站1号机组反应堆安全壳内实施调查结果显示，沉积在安全壳底部的高活度核污水中，测得辐射量为每小时1.5希沃特。　　据悉，该公司同时还拍摄到安全壳内部图像。这是首次测定安全壳内部的核污水辐射量并拍摄到清晰图像。资料图：机器人在福岛核电站内作业资料图：机器人在福岛核电站内作业　　测定位置距安全壳底部约1米。机器人行走的作业用脚手架附近的辐射量为每小时7.8希沃特。关于调查地点是否存在熔落核燃料(燃料碎片)，东电称：“正在对拍摄图像进行分析，无法判断。”今后还将从其它地点继续进行调查，力争首次确认燃料碎片的状况。　　据推断，1号机组几乎全部核燃料熔落，从反应堆压力容器穿透并掉入安全壳内，可能分布在安全壳底部污水之中。资料图：福岛核电站资料图：福岛核电站　　据了解，东电18日向安全壳内投入机器人。在位于压力容器地基的排水沟上方，向下垂放连接缆线的摄像头和辐射计进行调查。公布的图像包括设置在排水沟内部的泵阀，以及支撑管道的建筑物等。　　关于是否已拍摄到燃料碎片，东电说明称：“必须对包括其它调查地点获得的图像和辐射量进行分析才能判断。”　　脚手架附近为每小时7.8希沃特，与曾经在1号机组的安全壳内投入机器人调查时得到的结果基本相同。　　报道还称，本次调查，东电把棒状机器人从安全壳贯通部分的开孔处投入。机器人在一层作业用脚手架上变形为“コ”字形，移动到调查地点。机器人从脚手架的缝隙中向地下垂放摄像头和辐射计，在被放射性物质污染的水中进行拍摄和测量辐射。回顶部小编吐槽新媒体小灶街谈巷议夜光杯投诉图集视频聚合侬好上海侬好学堂页 头条 突发 新民眼 时政 社会 民生 科教 健康 法谭 财经 文娱圈 运动汇 环球 军事 夜光杯 国艺 国内 新媒体小灶 侬好上海 装潢 读晚报展开东电首次测得福岛核电站安全壳底部污水辐射量来源：中国新闻网   2017-03-20 09:30　　中新网3月20日电 据日本共同社报道，日本东京电力公司本月19日宣布，18日通过自动行走机器人对福岛第一核电站1号机组反应堆安全壳内实施调查结果显示，沉积在安全壳底部的高活度核污水中，测得辐射量为每小时1.5希沃特。　　据悉，该公司同时还拍摄到安全壳内部图像。这是首次测定安全壳内部的核污水辐射量并拍摄到清晰图像。资料图：机器人在福岛核电站内作业资料图：机器人在福岛核电站内作业　　测定位置距安全壳底部约1米。机器人行走的作业用脚手架附近的辐射量为每小时7.8希沃特。关于调查地点是否存在熔落核燃料(燃料碎片)，东电称：“正在对拍摄图像进行分析，无法判断。”今后还将从其它地点继续进行调查，力争首次确认燃料碎片的状况。　　据推断，1号机组几乎全部核燃料熔落，从反应堆压力容器穿透并掉入安全壳内，可能分布在安全壳底部污水之中。资料图：福岛核电站资料图：福岛核电站　　据了解，东电18日向安全壳内投入机器人。在位于压力容器地基的排水沟上方，向下垂放连接缆线的摄像头和辐射计进行调查。公布的图像包括设置在排水沟内部的泵阀，以及支撑管道的建筑物等。　　关于是否已拍摄到燃料碎片，东电说明称：“必须对包括其它调查地点获得的图像和辐射量进行分析才能判断。”　　脚手架附近为每小时7.8希沃特，与曾经在1号机组的安全壳内投入机器人调查时得到的结果基本相同。　　报道还称，本次调查，东电把棒状机器人从安全壳贯通部分的开孔处投入。机器人在一层作业用脚手架上变形为“コ”字形，移动到调查地点。机器人从脚手架的缝隙中向地下垂放摄像头和辐射计，在被放射性物质污染的水中进行拍摄和测量辐射。回顶部小编吐槽新媒体小灶街谈巷议夜光杯投诉图集视频聚合侬好上海侬好学堂页 头条 突发 新民眼 时政 社会 民生 科教 健康 法谭 财经 文娱圈 运动汇 环球 军事 夜光杯 国艺 国内 新媒体小灶 侬好上海 装潢 读晚报展开东电首次测得福岛核电站安全壳底部污水辐射量来源：中国新闻网   2017-03-20 09:30　　中新网3月20日电 据日本共同社报道，日本东京电力公司本月19日宣布，18日通过自动行走机器人对福岛第一核电站1号机组反应堆安全壳内实施调查结果显示，沉积在安全壳底部的高活度核污水中，测得辐射量为每小时1.5希沃特。　　据悉，该公司同时还拍摄到安全壳内部图像。这是首次测定安全壳内部的核污水辐射量并拍摄到清晰图像。资料图：机器人在福岛核电站内作业资料图：机器人在福岛核电站内作业　　测定位置距安全壳底部约1米。机器人行走的作业用脚手架附近的辐射量为每小时7.8希沃特。关于调查地点是否存在熔落核燃料(燃料碎片)，东电称：“正在对拍摄图像进行分析，无法判断。”今后还将从其它地点继续进行调查，力争首次确认燃料碎片的状况。　　据推断，1号机组几乎全部核燃料熔落，从反应堆压力容器穿透并掉入安全壳内，可能分布在安全壳底部污水之中。资料图：福岛核电站资料图：福岛核电站　　据了解，东电18日向安全壳内投入机器人。在位于压力容器地基的排水沟上方，向下垂放连接缆线的摄像头和辐射计进行调查。公布的图像包括设置在排水沟内部的泵阀，以及支撑管道的建筑物等。　　关于是否已拍摄到燃料碎片，东电说明称：“必须对包括其它调查地点获得的图像和辐射量进行分析才能判断。”　　脚手架附近为每小时7.8希沃特，与曾经在1号机组的安全壳内投入机器人调查时得到的结果基本相同。　　报道还称，本次调查，东电把棒状机器人从安全壳贯通部分的开孔处投入。机器人在一层作业用脚手架上变形为“コ”字形，移动到调查地点。机器人从脚手架的缝隙中向地下垂放摄像头和辐射计，在被放射性物质污染的水中进行拍摄和测量辐射。回顶部小编吐槽新媒体小灶街谈巷议夜光杯投诉图集视频聚合侬好上海侬好学堂页 头条 突发 新民眼 时政 社会 民生 科教 健康 法谭 财经 文娱圈 运动汇 环球 军事 夜光杯 国艺 国内 新媒体小灶 侬好上海 装潢 读晚报展开东电首次测得福岛核电站安全壳底部污水辐射量来源：中国新闻网   2017-03-20 09:30　　中新网3月20日电 据日本共同社报道，日本东京电力公司本月19日宣布，18日通过自动行走机器人对福岛第一核电站1号机组反应堆安全壳内实施调查结果显示，沉积在安全壳底部的高活度核污水中，测得辐射量为每小时1.5希沃特。　　据悉，该公司同时还拍摄到安全壳内部图像。这是首次测定安全壳内部的核污水辐射量并拍摄到清晰图像。资料图：机器人在福岛核电站内作业资料图：机器人在福岛核电站内作业　　测定位置距安全壳底部约1米。机器人行走的作业用脚手架附近的辐射量为每小时7.8希沃特。关于调查地点是否存在熔落核燃料(燃料碎片)，东电称：“正在对拍摄图像进行分析，无法判断。”今后还将从其它地点继续进行调查，力争首次确认燃料碎片的状况。　　据推断，1号机组几乎全部核燃料熔落，从反应堆压力容器穿透并掉入安全壳内，可能分布在安全壳底部污水之中。资料图：福岛核电站资料图：福岛核电站　　据了解，东电18日向安全壳内投入机器人。在位于压力容器地基的排水沟上方，向下垂放连接缆线的摄像头和辐射计进行调查。公布的图像包括设置在排水沟内部的泵阀，以及支撑管道的建筑物等。　　关于是否已拍摄到燃料碎片，东电说明称：“必须对包括其它调查地点获得的图像和辐射量进行分析才能判断。”　　脚手架附近为每小时7.8希沃特，与曾经在1号机组的安全壳内投入机器人调查时得到的结果基本相同。　　报道还称，本次调查，东电把棒状机器人从安全壳贯通部分的开孔处投入。机器人在一层作业用脚手架上变形为“コ”字形，移动到调查地点。机器人从脚手架的缝隙中向地下垂放摄像头和辐射计，在被放射性物质污染的水中进行拍摄和测量辐射。回顶部小编吐槽新媒体小灶街谈巷议夜光杯投诉图集视频聚合侬好上海侬好学堂页 头条 突发 新民眼 时政 社会 民生 科教 健康 法谭 财经 文娱圈 运动汇 环球 军事 夜光杯 国艺 国内 新媒体小灶 侬好上海 装潢 读晚报展开东电首次测得福岛核电站安全壳底部污水辐射量来源：中国新闻网   2017-03-20 09:30　　中新网3月20日电 据日本共同社报道，日本东京电力公司本月19日宣布，18日通过自动行走机器人对福岛第一核电站1号机组反应堆安全壳内实施调查结果显示，沉积在安全壳底部的高活度核污水中，测得辐射量为每小时1.5希沃特。　　据悉，该公司同时还拍摄到安全壳内部图像。这是首次测定安全壳内部的核污水辐射量并拍摄到清晰图像。资料图：机器人在福岛核电站内作业资料图：机器人在福岛核电站内作业　　测定位置距安全壳底部约1米。机器人行走的作业用脚手架附近的辐射量为每小时7.8希沃特。关于调查地点是否存在熔落核燃料(燃料碎片)，东电称：“正在对拍摄图像进行分析，无法判断。”今后还将从其它地点继续进行调查，力争首次确认燃料碎片的状况。　　据推断，1号机组几乎全部核燃料熔落，从反应堆压力容器穿透并掉入安全壳内，可能分布在安全壳底部污水之中。资料图：福岛核电站资料图：福岛核电站　　据了解，东电18日向安全壳内投入机器人。在位于压力容器地基的排水沟上方，向下垂放连接缆线的摄像头和辐射计进行调查。公布的图像包括设置在排水沟内部的泵阀，以及支撑管道的建筑物等。　　关于是否已拍摄到燃料碎片，东电说明称：“必须对包括其它调查地点获得的图像和辐射量进行分析才能判断。”　　脚手架附近为每小时7.8希沃特，与曾经在1号机组的安全壳内投入机器人调查时得到的结果基本相同。　　报道还称，本次调查，东电把棒状机器人从安全壳贯通部分的开孔处投入。机器人在一层作业用脚手架上变形为“コ”字形，移动到调查地点。机器人从脚手架的缝隙中向地下垂放摄像头和辐射计，在被放射性物质污染的水中进行拍摄和测量辐射。回顶部小编吐槽新媒体小灶街谈巷议夜光杯投诉图集视频聚合侬好上海侬好学堂";
        System.out.println("=====================公钥加密，私钥解密=======================");
        String encryptData = rsaUtil.encrypt(publicKeyStr, data);
        System.out.println("公钥加密后数据：" + encryptData);
        String decryptData = rsaUtil.decrypt(privateKeyStr, encryptData);
        System.out.println("私钥解密后数据：" + decryptData);
        System.out.println("加解密后数据是否与原数据相同：" + data.equals(decryptData));

        System.out.println("=====================私钥生成sign，公钥验签=======================");
        String sign = rsaUtil.generateSign(privateKeyStr, data);
        System.out.println("使用私钥生成sign值：" + sign);
        boolean verify = rsaUtil.verifyRSA(publicKeyStr, data, sign);
        System.out.println("使用公钥验签sign值：" + verify);

        System.out.println("=====================私钥加密，公钥解密=======================");
        String encryptByPrivateKey = rsaUtil.encryptByPrivateKey(privateKeyStr, data);
        System.out.println("私钥加密后数据：" + encryptByPrivateKey);
        String decryptByPublicKey = rsaUtil.decryptByPublicKey(publicKeyStr, encryptByPrivateKey);
        System.out.println("公钥解密后数据：" + decryptByPublicKey);
        System.out.println("加解密后数据是否与原数据相同" + data.equals(decryptByPublicKey));

    }
}
