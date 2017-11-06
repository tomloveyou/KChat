/**
 * Copyright 2014 Joan Zapata
 *
 * This file is part of Android-pdfview.
 *
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mfc.appframe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    private NumberUtils() {
        // Prevents instantiation
    }

    /**
     * Limits the given <b>number</b> between the other values
     * @param number  The number to limit.
     * @param between The smallest value the number can take.
     * @param and     The biggest value the number can take.
     * @return The limited number.
     */
    public static int limit(int number, int between, int and) {
        if (number <= between) {
            return between;
        }
        if (number >= and) {
            return and;
        }
        return number;
    }
    /**
     * 从字符串中截取连续6位数字
     * 用于从短信中获取动态密码
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
     Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
     Matcher m = continuousNumberPattern.matcher(str);
     String dynamicPassword = "";
     while(m.find()){
      if(m.group().length() == 5) {
       System.out.print(m.group());
       dynamicPassword = m.group();
      }
     }
     
     return dynamicPassword;
    }
}
