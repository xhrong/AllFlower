package com.tietuku.entity.util;

public class Base64Util {
	public static String base64(byte[] target){
        String temp=android.util.Base64.encodeToString(target,0);
        return temp.replace('+', '-').replace('/', '_');
	}
}
