package mizuki.project.core.restserver.util;

import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	private final static DateParser sdfTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	private final static DateParser sdfDate = FastDateFormat.getInstance("yyyy-MM-dd");
	private final static FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static String formatStandard(long times) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(times);
		return formatStandard(calendar);
	}

	public static String formatStandard(Calendar calendar) {
		return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE)
				+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
	}

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
		return String.format("%02d",(calendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",calendar.get(Calendar.DATE));
	}

	public static Timestamp parseStamp(String datetime){
		try {
			Date date = sdfTime.parse(datetime);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 间隔多少天 注意 参数的值会更改
	 */
	public static long computeIntervalDays(Calendar a,Calendar b){
		clearHMS(a);
		clearHMS(b);
		return (b.getTimeInMillis()-a.getTimeInMillis())/(24*60*60*1000)-1;
	}

	public static String formatSdfTimeFromTimes(long times){
		return fastDateFormat.format(new Date(times));
	}

	public static Timestamp formatTimestamp(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return new Timestamp(fmt.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 */
	public static boolean isValidDate(String s) {
		try {
			sdfDate.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static String formatMillSecondHMS(long sec,boolean zh){
		sec = sec/1000;
		int hh=(int)Math.floor(sec/60/60);
		int mm=(int)Math.floor(sec/60)%60;
		String ret="";
		if(zh){
			// ret += (dd===0?"0":dd)+"天";
			ret += (hh==0?"0":hh)+"时";
			ret += (mm==0?"0":mm)+"分";
			ret += sec%60+"秒";
		}else {
			// ret += (dd===0?"00":dd)+":";
			ret += (hh == 0 ? "00" : String.format("%2d", hh)) + ":";
			ret += (mm == 0 ? "00" : String.format("%2d", mm)) + ":";
			ret += String.format("%2d", sec % 60);
		}
		return ret;
	}

	public static String formatMillSecondHM(long sec,boolean zh){
		sec = sec/1000;
		int hh=(int)Math.floor(sec/60/60);
		int mm=(int)Math.floor(sec/60)%60;
		String ret="";
		if(zh){
			ret += (hh==0?"0":hh)+"时";
			ret += (mm==0?"0":mm)+"分";
		}else {
			ret += (hh == 0 ? "00" : String.format("%2d", hh)) + ":";
			ret += (mm == 0 ? "00" : String.format("%2d", mm));
		}
		return ret;
	}

	/**
	 * 把时间根据时、分、秒转换为时间段
	 */
	public static String getTimes(String StrDate) {
		String resultTimes = "";
		Date now;
		try {
			now = new Date();
			Date date = sdfTime.parse(StrDate);
			long times = now.getTime() - date.getTime();
			long day = times / (24 * 60 * 60 * 1000);
			long hour = (times / (60 * 60 * 1000) - day * 24);
			long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			StringBuffer sb = new StringBuffer();
			// sb.append("发表于：");
			if (hour > 0) {
				sb.append(hour + "小时前");
			} else if (min > 0) {
				sb.append(min + "分钟前");
			} else {
				sb.append(sec + "秒前");
			}

			resultTimes = sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return resultTimes;
	}


}
