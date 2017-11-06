package com.mfc.appframe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common Validator
 */
public class IdCardValidator {

    public static final String IP_ADDRESS_REGEX = "(https?://|ftp://|file://|www)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String CHAT_FORPRODUCT_REGEX = "http://([0-9a-zA-Z_-]+\\.)*webuy-china\\.com/product\\?ref=[0-9a-zA-Z]*";
    public static final String PASSWORD_REGEX = "^[\\w\\p{Punct}]{6,12}$";
    public static final String DEVICE_ID_REGEX = "^[1-9]\\d{15}";
    public static final String EMAIL_REGEX = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    public static final String ID_CARD_NUMBER_REGEX = "(\\d{14}[0-9X])|(\\d{17}[0-9X])";
//	public static final String PHONE_REGEX 		= "^[\\d\\-\\+#\\s]{5,30}$";
//	public static final String ACCOUNT_REGEX 	= "^[\\da-z]{2,20}$";

    //测试用的
    public static final String PHONE_REGEX = "1[3|5|7|8|][0-9]{9}";
    public static final String ACCOUNT_REGEX = "^[a-zA-Z1-9]\\w{1,17}";

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEqual(Object obj1, Object obj2) {
        return obj1 != null && obj1.equals(obj2);
    }

    public static boolean lengthLargeThan(String string, int maxLength) {
        return string != null && string.length() > maxLength;
    }

    public static boolean lengthLessThan(String string, int minLength) {
        return string != null && string.length() < minLength;
    }

    public static boolean lengthBetween(String string, int minLength, int maxLength) {
        return !lengthLessThan(string, minLength) && !lengthLargeThan(string, maxLength);
    }

    public static boolean isDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean match(String string, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public static boolean isPhoneValid(String mobiles) {
        if (isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmailValid(String _email) {
        if (isEmpty(_email)) {
            return false;
        }
        Pattern p = Pattern.compile(EMAIL_REGEX);
        Matcher m = p.matcher(_email);
        return m.matches();
    }

    /**
     * 验证身份证号码
     *
     * @param _idCardNumber 字符串
     * @return true 为身份证号码，反之返回false
     */
    public static boolean isIdCardNumberValid(String _idCardNumber) {
        if (isEmpty(_idCardNumber)) {
            return false;
        }
        Pattern p = Pattern.compile(ID_CARD_NUMBER_REGEX);
        Matcher m = p.matcher(_idCardNumber);
//		return m.matches();

        if (m.matches()) {
            List<Integer> list_cityCode = new ArrayList<>();
            list_cityCode.add(11);
            list_cityCode.add(12);
            list_cityCode.add(13);
            list_cityCode.add(14);
            list_cityCode.add(15);
            list_cityCode.add(21);
            list_cityCode.add(22);
            list_cityCode.add(23);
            list_cityCode.add(31);
            list_cityCode.add(32);
            list_cityCode.add(33);
            list_cityCode.add(34);
            list_cityCode.add(35);
            list_cityCode.add(36);
            list_cityCode.add(37);
            list_cityCode.add(41);
            list_cityCode.add(42);
            list_cityCode.add(43);
            list_cityCode.add(44);
            list_cityCode.add(45);
            list_cityCode.add(46);
            list_cityCode.add(50);
            list_cityCode.add(51);
            list_cityCode.add(52);
            list_cityCode.add(53);
            list_cityCode.add(54);
            list_cityCode.add(61);
            list_cityCode.add(62);
            list_cityCode.add(63);
            list_cityCode.add(64);
            list_cityCode.add(65);
            list_cityCode.add(71);
            list_cityCode.add(81);
            list_cityCode.add(82);
            list_cityCode.add(91);
            int cityCode = Integer.parseInt(_idCardNumber.substring(0, 2));
            if (list_cityCode.contains(cityCode)) {//验证城市地区

                //验证年份
                int year = Integer.parseInt(_idCardNumber.substring(6, 11));
                if (year < Calendar.getInstance().YEAR) {

                } else {
                    return false;
                }
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    //身份证号码验证：start

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     */
    public static String IDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后一位都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (!isDataFormat(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(
                strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                errorInfo = "身份证无效，不是合法的身份证号码,如果含有字母，字母请大写";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }


    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    //身份证号码验证：end

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public static boolean isDefaultAvatar(int _avatarVersion) {
        return _avatarVersion == 1;
    }

    public static boolean isNumeric(String _str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(_str).matches();
    }

    /**
     * 判断是否为空
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