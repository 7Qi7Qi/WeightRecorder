package com.example.yui.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemUtils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd HH:mm");
    private static final SimpleDateFormat FORMAT1 = new SimpleDateFormat("MM月dd日");

    public static String formatDateDay(Date date) {
        if (date == null) {
            return "";
        }
        return FORMAT1.format(date);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return FORMAT.format(date);
    }

    public static long getDateDiff(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() - date2.getTime();
        }
        return 0;
    }
}
