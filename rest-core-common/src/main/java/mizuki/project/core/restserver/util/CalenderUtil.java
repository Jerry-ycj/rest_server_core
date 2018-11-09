package mizuki.project.core.restserver.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderUtil {

    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void clearHMS(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
    }

    public static String formatYMD(Calendar calendar){
        return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
    }

    public static String formatMD(Calendar calendar){
        return (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
    }

    /**
     * 零点
     */
    public static long getZeroPoint(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    /**
     * 间隔多少天
     */
    public static long computeIntervalDays(Calendar a,Calendar b){
        clearHMS(a);
        clearHMS(b);
        return (b.getTimeInMillis()-a.getTimeInMillis())/(24*60*60*1000)-1;
    }

    public static Timestamp parseStamp(String datetime){
        try {
            Date date = sdfTime.parse(datetime);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
