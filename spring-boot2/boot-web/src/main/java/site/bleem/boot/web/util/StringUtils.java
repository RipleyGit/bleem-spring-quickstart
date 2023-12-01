package site.bleem.boot.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yubs
 * @desc todo
 * @date 2023/12/1
 */
public class StringUtils {
    public static String getMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());

        byte[] digest = md.digest();

        // Convert the byte array to a hex string
        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
