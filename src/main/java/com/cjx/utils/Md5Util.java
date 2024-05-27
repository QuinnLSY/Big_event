package com.cjx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Util工具类提供了MD5加密相关的功能。
 */
public class Md5Util {
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    // 表示16进制值的数组
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // 用于计算md5值的类
    protected static MessageDigest messagedigest = null;
    // 静态代码块，初始化messagedigest
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(Md5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    /**
     * 将字符串转换为MD5加密后的字符串。
     * 此方法通过调用 {@link #getMD5String(byte[])} 方法，将字符串转换为字节数组后进行MD5加密。
     *
     * @param s 需要加密的字符串。
     * @return 经过MD5加密后的字符串。
     */
    public static String getMD5String(String s) {
        // 将字符串转换为字节数组后进行MD5加密
        return getMD5String(s.getBytes());
    }


    /**
     * 检查明文密码和经过MD5加密的密码字符串是否匹配。
     * 这个方法首先将明文密码通过MD5算法加密，然后将加密后的字符串与传入的MD5密码字符串进行比较，
     * 以确定两者是否匹配。
     *
     * @param password 明文密码，需要被检查的原始密码。
     * @param md5PwdStr 经过MD5加密的密码字符串，用于比较的加密后密码。
     * @return 如果明文密码经过MD5加密后和给定的MD5密码字符串相等，则返回true，否则返回false。
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        // 对明文密码进行MD5加密
        String s = getMD5String(password);
        // 比较加密后的字符串与给定的MD5密码字符串是否相等
        return s.equals(md5PwdStr);
    }


    /**
     * 将字节数组转换为经过MD5加密后的字符串。
     * <p>此方法使用MD5算法对输入的字节数组进行加密，然后将加密后的结果转换为16进制字符串。</p>
     *
     * @param bytes 需要加密的字节数组。这是要进行MD5处理的数据。
     * @return 经过MD5加密后的字符串。返回的是32位小写十六进制字符串。
     */
    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes); // 更新消息摘要，以字节数组为输入进行MD5处理
        return bufferToHex(messagedigest.digest()); // 将处理后的消息摘要转换为十六进制字符串并返回
    }


    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 需要转换的字节数组
     * @return 转换后的十六进制字符串
     */
    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    /**
     * 将字节数组的指定范围转换为十六进制字符串。
     *
     * @param bytes 需要转换的字节数组。
     * @param m 起始索引，表示从字节数组的第m个元素开始转换。
     * @param n 结束索引，表示转换到字节数组的第n个元素（不包括第n个元素）。
     * @return 转换后的十六进制字符串。
     */
    private static String bufferToHex(byte[] bytes, int m, int n) {
        // 创建一个字符串缓冲区，用于存储转换后的十六进制字符串
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n; // 计算需要转换的字节数

        // 遍历指定范围的字节数组，将其转换为十六进制字符串并添加到字符串缓冲区中
        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        // 返回转换后的十六进制字符串
        return stringbuffer.toString();
    }


    /**
     * 将一个字节转换为两个十六进制字符。
     * 该方法通过按位操作和位移将字节分解成两个四位的十六进制数，
     * 然后将这两个十六进制数转换为对应的字符，最后将字符添加到提供的StringBuffer中。
     *
     * @param bt 需要转换的字节。一个字节可以无损地转换为两个四位的十六进制数。
     * @param stringbuffer 存放转换结果的StringBuffer。转换后的两个十六进制字符将被添加到此StringBuffer的末尾。
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        // 将输入字节的高4位转换为十六进制字符
        char c0 = hexDigits[(bt & 240) >> 4];
        // 将输入字节的低4位转换为十六进制字符
        char c1 = hexDigits[bt & 15];
        // 将转换后的两个十六进制字符依次添加到stringbuffer中
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
