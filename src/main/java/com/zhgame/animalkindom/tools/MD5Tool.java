package com.zhgame.animalkindom.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {
	
	public static String getEncrypted(String content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(content.getBytes());
        return bytes2Hex(md.digest());
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
