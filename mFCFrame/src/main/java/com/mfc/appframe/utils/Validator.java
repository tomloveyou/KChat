package com.mfc.appframe.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common Validator
 */
public class Validator {
	
	public static final String IP_ADDRESS_REGEX		= "(https?://|ftp://|file://|www)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static final String CHAT_FORPRODUCT_REGEX= "http://([0-9a-zA-Z_-]+\\.)*webuy-china\\.com/product\\?ref=[0-9a-zA-Z]*";
	public static final String PASSWORD_REGEX 		= "^[\\w\\p{Punct}]{6,12}$";
	public static final String DEVICE_ID_REGEX 		= "^[1-9]\\d{15}";
	public static final String EMAIL_REGEX 			= "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public static final String ID_CARD_NUMBER_REGEX = "(\\d{14}[0-9X])|(\\d{17}[0-9X])";
//	public static final String PHONE_REGEX 		= "^[\\d\\-\\+#\\s]{5,30}$";
//	public static final String ACCOUNT_REGEX 	= "^[\\da-z]{2,20}$";
	
	//测试用的
	public static final String PHONE_REGEX 		= "1[3|5|7|8|][0-9]{9}";
	public static final String ACCOUNT_REGEX    ="^[a-zA-Z1-9]\\w{1,17}";

	public static boolean isEmpty(String string){
		return string == null || string.length() == 0;
	}
	
	public static boolean isEmpty(List<?> list){
		return list == null || list.isEmpty();
	}
		
	public static boolean isEqual(Object obj1,Object obj2){
		return obj1 != null && obj1.equals(obj2);
	}
	
	public static boolean lengthLargeThan(String string,int maxLength){
		return string != null && string.length() > maxLength;
	}
	
	public static boolean lengthLessThan(String string,int minLength){
		return string != null && string.length() < minLength;		
	}
	
	public static boolean lengthBetween(String string,int minLength,int maxLength){
		return !lengthLessThan(string,minLength) && !lengthLargeThan(string,maxLength);
	}
	
	public static boolean isDouble(String number) {
		try {
			Double.parseDouble(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean match(String string,String regex){		
		Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        return m.matches();
	}
	
	public static boolean isPhoneValid(String mobiles) {
		if(isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern.compile("^((17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	public static boolean isEmailValid(String _email) {
		if(isEmpty(_email)) {
			return false;
		}
		Pattern p = Pattern.compile(EMAIL_REGEX);
		Matcher m = p.matcher(_email);
		return m.matches();
	}
	public static boolean isIdCardNumberValid(String _idCardNumber) {
		if(isEmpty(_idCardNumber)) {
			return false;
		}
		Pattern p = Pattern.compile(ID_CARD_NUMBER_REGEX);
		Matcher m = p.matcher(_idCardNumber);
		return m.matches();
	}
	
	public static boolean isPasswordValid(String password) {
		return password.length() >= 6;
	}
	
	public static boolean isDefaultAvatar(int _avatarVersion) {
		return _avatarVersion == 1;
	}
	
	public static boolean isNumeric(String _str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(_str).matches();    
	} 
	
	/**
	 * 判断是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isnotNull(Object obj) {
		boolean b = false;
		if (null != obj && !obj.toString().equals("")
				&& !obj.toString().toLowerCase().equals("null")
				&& "undefined" != obj) {
			b = true;
		}
		return b;
	}
	
	
}