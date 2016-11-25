package upload.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * author：created by Snail.江
 * time: 7/26/2016 15:26
 * email：409962004@qq.com
 * TODO:
 */
public class HexUtils {

    private final static String HEX_NUMS_STR = "0123456789ABCDEF";
    private final static Integer SALT_LENGTH = 12;

    /**
     * 将16进制字符串转换成数组
     *
     * @return byte[]
     * @author jacob
     */
    public static byte[] hexStringToByte(String hex) {
        /* len为什么是hex.length() / 2 ?
         * 首先，hex是一个字符串，里面的内容是像16进制那样的char数组
         * 用2个16进制数字可以表示1个byte，所以要求得这些char[]可以转化成什么样的byte[]，首先可以确定的就是长度为这个char[]的一半
         */
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;
    }

    /**
     * 将数组转换成16进制字符串
     *
     * @return String
     * @author jacob
     */
    public static String byteToHexString(byte[] salt) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < salt.length; i++) {
            String hex = Integer.toHexString(salt[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 密码验证
     *
     * @param passwd   用户输入密码
     * @param dbPasswd 数据库保存的密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static boolean validPasswd(String passwd, String dbPasswd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwIndb = hexStringToByte(dbPasswd);
        //定义salt
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(pwIndb, 0, salt, 0, SALT_LENGTH);
        //创建消息摘要对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        //将盐数据传入消息摘要对象
        md.update(salt);
        md.update(passwd.getBytes("UTF-8"));
        byte[] digest = md.digest();
        //声明一个对象接收数据库中的口令消息摘要
        byte[] digestIndb = new byte[pwIndb.length - SALT_LENGTH];
        //获得数据库中口令的摘要
        System.arraycopy(pwIndb, SALT_LENGTH, digestIndb, 0, digestIndb.length);
        //比较根据输入口令生成的消息摘要和数据库中的口令摘要是否相同
        //口令匹配相同
        return Arrays.equals(digest, digestIndb);
    }

//    /**
//     * 获得md5之后的16进制字符
//     *
//     * @param passwd 用户输入密码字符
//     * @return String md5加密后密码字符
//     * @throws NoSuchAlgorithmException
//     * @throws UnsupportedEncodingException
//     */
//    public static String toHexString(String passwd) {
//        //拿到一个随机数组，作为盐
//        byte[] pwd = null;
//        SecureRandom sc = new SecureRandom();
//        byte[] salt = new byte[SALT_LENGTH];
//        sc.nextBytes(salt);
//
//        //声明摘要对象，并生成
//        MessageDigest md = null;
//        try {
//            md = MessageDigest.getInstance("MD5");
//            md.update(salt);
//            md.update(passwd.getBytes("UTF-8"));
//            byte[] digest = md.digest();
//
//            pwd = new byte[salt.length + digest.length];
//            System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
//            System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
//            return byteToHexString(pwd);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//       return "";
//    }

    public static String toHexString(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
