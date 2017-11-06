package com.mfc.appframe.algorithm;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DESede {
	//===================加密秘钥==========================
	// 加密－－ENCODE
	public static final String SKEY = "6FGF1OSJJ9O3";
	// 解密－－DECODE
	public static final String SIV = "OV3Z1FOD";
	private final static String DESede = "DESede/CBC/PKCS5Padding";

	// 传入keystr 12位 返回一个byte数组
	public static byte[] getSKey(String keystr) {
		// 得到反向排序的String
		String tempStr = getString(keystr);
		// 构建一个新的字符串（正+反+反+正+正+反）72位
		String newStr = keystr + tempStr + tempStr + keystr + keystr + tempStr;
		int l = newStr.length() / 3;
		byte[] w = new byte[l];
		// 为byte数组传值
		for (int i = 0; i < l; i++) {
			// parseInt(String s, int radix) 使用第二个参数指定的基数，将字符串参数解析为有符号的整数。
			w[i] = (byte) (Integer.parseInt(newStr.substring(i * 3, i * 3 + 3),
					36) & 0xff);
		}
		return (w);
	}

	// 传入sIV 8位 返回一个byte数组
	public static byte[] getSIV(String sIV) {
		// 得到反向排序的String
		String tempStr = getString(sIV);
		// 构建一个新的字符串（正+反+正+反）32位
		String newStr = sIV + tempStr + sIV + tempStr;
		int l = newStr.length() / 4;
		byte[] w = new byte[l];
		for (int i = 0; i < l; i++) {
			w[i] = (byte) (Integer.parseInt(newStr.substring(i * 4, i * 4 + 4),
					36) & 0xff);
		}
		return (w);
	}

	// 传入一个字符串返回一个反向排序的字符串
	public static String getString(String keystr) {
		int l = keystr.length();
		char data[] = keystr.toCharArray();
		char str[] = new char[l];
		for (int i = 0, j = l - 1; i < l; i++, j--) {
			str[j] = data[i];
		}
		String strKey = new String(str);
		return strKey;
	}

	// 传入明文byte[]数组，12位sKey，8位sIV返回加密处理的byte[]数组
	private static byte[] EncryptionByteData(byte[] SourceData, String sKey,
			String sIV) throws Exception {
		byte[] retByte = null;
		byte[] EncryptionByte = getSKey(sKey);
		SecretKey securekey = new SecretKeySpec(EncryptionByte, "DESede");
		IvParameterSpec spec = new IvParameterSpec(getSIV(sIV));
		Cipher cipher = Cipher.getInstance(DESede);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, spec);
		retByte = cipher.doFinal(SourceData);
		return retByte;
	}

	// 传入密文byte[]数组，12位sKey，8位sIV 返回解密处理的byte[]数组
	private static byte[] DecryptionByteData(byte[] SourceData, String sKey,
			String sIV) throws Exception {
		byte[] retByte = null;
		byte[] EncryptionByte = getSKey(sKey);
		SecretKey securekey = new SecretKeySpec(EncryptionByte, "DESede");
		IvParameterSpec spec = new IvParameterSpec(getSIV(sIV));
		Cipher cipher = Cipher.getInstance(DESede);
		cipher.init(Cipher.DECRYPT_MODE, securekey, spec);
		retByte = cipher.doFinal(SourceData);
		return retByte;
	}

	// 传入明文，12位sKey，8位sIV，生成密文
	public static String EncryptCode(String strTobeEnCrypted, String sKey,
			String sIV) throws Exception {
		String retStr = null;
		byte[] retByte = null;
		byte[] sorData = strTobeEnCrypted.getBytes();
		retByte = EncryptionByteData(sorData, sKey, sIV);
		BASE64Encoder be = new BASE64Encoder();
		retStr = be.encode(retByte);
		return retStr;
	}

	// 传入密文，12位sKey，8位sIV，生成明文
	public static String DecryptCode(String strTobeDeCrypted, String sKey,
			String sIV) throws Exception {
		String retStr = null;
		byte[] retByte = null;
		BASE64Decoder bd = new BASE64Decoder();
		byte[] sorData = bd.decodeBuffer(strTobeDeCrypted);
		retByte = DecryptionByteData(sorData, sKey, sIV);
		retStr = new String(retByte);
		return retStr;
	}

	// 传入明文，12位sKey，8位sIV，生成密文
	public static String EncryptCodeUTF8(String strTobeEnCrypted, String sKey,
			String sIV) throws Exception {
		String retStr = null;
		byte[] retByte = null;
		byte[] sorData = strTobeEnCrypted.getBytes();
		retByte = EncryptionByteData(sorData, sKey, sIV);
		BASE64Encoder be = new BASE64Encoder();
		retStr = be.encode(retByte);
		return retStr;
	}

	// 传入密文，12位sKey，8位sIV，生成明文
	public static String DecryptCodeUTF8(String strTobeDeCrypted, String sKey,
			String sIV) throws Exception {
		String retStr = null;
		byte[] retByte = null;
		BASE64Decoder bd = new BASE64Decoder();
		byte[] sorData = bd.decodeBuffer(strTobeDeCrypted);
		retByte = DecryptionByteData(sorData, sKey, sIV);
		retStr = new String(retByte, "UTF-8");
		return retStr;
	}
	
	// 传入密文，12位sKey，8位sIV，生成明文
	public static String DecryptCodeGBK(String strTobeDeCrypted, String sKey,
			String sIV) throws Exception {
		String retStr = null;
		byte[] retByte = null;
		BASE64Decoder bd = new BASE64Decoder();
		byte[] sorData = bd.decodeBuffer(strTobeDeCrypted);
		retByte = DecryptionByteData(sorData, sKey, sIV);
		retStr = new String(retByte,"GBK");
		return retStr;
	}
}
