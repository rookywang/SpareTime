package priv.ky2.sparetime.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将日期从lLong 类型转换成 String 类型
 *
 * @author wangkaiyan
 * @date 2017/4/18.
 */

public class DateFormatter {

    private static final String ZH_DATE_FORMAT = "yyyyMMdd";
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";

    public String zhDateFormat(long date) {
        String sDate;
        Date d = new Date(date + 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat(ZH_DATE_FORMAT);
        sDate = format.format(d);
        return sDate;
    }

    public String dbDateFormat(long date) {
        String sDate;
        Date d = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat(DB_DATE_FORMAT);
        sDate = format.format(d);
        return sDate;
    }
}
