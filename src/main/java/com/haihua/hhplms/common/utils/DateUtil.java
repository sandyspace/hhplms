package com.haihua.hhplms.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

}
