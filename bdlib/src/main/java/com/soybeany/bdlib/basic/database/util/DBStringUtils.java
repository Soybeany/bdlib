package com.soybeany.bdlib.basic.database.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DBStringUtils {

    // //////////////////////////////////FORMAT区域//////////////////////////////////

    /**
     * 将字符串中的参数替换为真实参数
     */
    public static String format(String format, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            format = format.replace("{" + i + "}", (args[i] != null) ? args[i].toString() : "null");
        }
        return format;
    }


    // //////////////////////////////////JOIN区域//////////////////////////////////

    /**
     * 拼接列表中的字符串
     */
    public static String join(List<String> valueList, String separator) {
        if ((valueList == null) || (valueList.isEmpty())) {
            return "";
        }

        Iterator iterator = valueList.iterator();
        StringBuilder builder = new StringBuilder((String) iterator.next());
        while (iterator.hasNext()) {
            builder.append(separator).append((String) iterator.next());
        }
        return builder.toString();
    }

    /**
     * 拼接列表中的字符串
     */
    public static String join(String[] valueList, String separator) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, valueList);
        return join(list, separator);
    }

}
