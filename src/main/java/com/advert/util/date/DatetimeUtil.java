
package com.advert.util.date;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**

 *
 */
public class DatetimeUtil {

	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD2 = "yyyy/MM/dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS2 = "yyyy/MM/dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS3 = "yyyy_MM_dd_HH_mm_ss";
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YYYYMMDDHHMMSSsss = "yyyyMMddHHmmssSSS";
	public static final String HH_MM = "HHmm";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	// array reprsents offset bewwen Monday and current date, start from Sunday
	private static final int[] offsets = { -6, 0, -1, -2, -3, -4, -5 };

	// day of week string array
	public static final String[] dayOfWk = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	/**
	 * Change the java.util.date format to java.sql.Timestamp format
	 * 
	 * @param java.util.Date date
	 * @return java.sql.Timestamp
	 */
	public static Timestamp dateToTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * Change the java.sql.Timestamp format to java.util.Date format
	 * 
	 * @param java.sql.Timestamp
	 * @param string format
	 * @return java.util.Date
	 */
	public static Date timestampToDate(Timestamp timestamp, String format) {
		if ((timestamp == null) || (format == null)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(timestampToString(timestamp, format));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Change the java.sql.Timestamp format to java.util.Date format
	 * 
	 * @param java.sql.Timestamp
	 * @return java.util.Date
	 */
	public static Date timestampToDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	/**
	 * Change the java.sql.Timestamp format to java.util.String format
	 * 
	 * @param java.sql.Timestamp
	 * @return String
	 */
	public static String timestampToString(Timestamp timestamp, String format) {
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat(format);
			return myFormatter.format(DatetimeUtil.timestampToDate(timestamp));
		} catch (Exception err) {
			return "";
		}
	}

	/**
	 * Change the java.sql.Timestamp format to java.util.String format
	 * 
	 * @param java.sql.Timestamp
	 * @return String
	 */
	public static String dateToString(Date date, String format) {
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat(format);
			return myFormatter.format(date);
		} catch (Exception err) {
			return "";
		}
	}

	/**
	 * Get the current date
	 * 
	 * @return java.util.Date date
	 */
	public static Date getCurrentDate() {
		return new java.util.Date();
	}

	/**
	 * Get the current Timestamp
	 * 
	 * @return java.sql.Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Get the current date according to specific format
	 * 
	 * @param String
	 * @return String
	 */
	public static String getCurrentDate(String format) {
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat(format);
			return myFormatter.format(DatetimeUtil.getCurrentDate());
		} catch (Exception err) {
			return "";
		}
	}

	/**
	 * Get the date according to specific String content
	 * The default format is "yyyy-MM-dd"
	 * 
	 * @param String
	 * @return Date
	 */
	public static Date stringToDate(String datecontent, String format) {
		if ((format == null) || format.equals("")) {
			format = "yyyy-MM-dd";
		}

		try {
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
			Date date = bartDateFormat.parse(datecontent);
			return date;
		} catch (ParseException pe) {
			String message = "Exception occurs in Parse progress.";
		} catch (Exception e) {
			String message = "Exception occurs during the string converting to Date.";
		}
		return null;
	}

	/**
	 * Get the start date - end date string before current week according to week number
	 * specified by parameter
	 * The format is "yyyy/MM/dd-yyyy/MM/dd"
	 * 
	 * @param int weekNum
	 * @return SortedMap
	 */
	public static String getThisWeek() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", new DateFormatSymbols(Locale.US));

		int day = now.get(Calendar.DAY_OF_WEEK);
		int offset = offsets[day - Calendar.SUNDAY] - 1;
		now.add(Calendar.DAY_OF_YEAR, offset);
		String temp = null;

		now.add(Calendar.DAY_OF_YEAR, +1);
		temp = formatter.format(now.getTime());
		now.add(Calendar.DAY_OF_YEAR, +6);
		temp += "-" + formatter.format(now.getTime());

		return temp;
	}

	/**
	 * Get the start date - end date string before current week according to week number
	 * specified by parameter
	 * The format is "yyyy/MM/dd-yyyy/MM/dd"
	 * 
	 * @param int weekNum
	 * @return SortedMap
	 */
	public static SortedMap getPreWeeks(int weekNum) {
		SortedMap m = new TreeMap();
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, -(7 * (weekNum - 1)));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", new DateFormatSymbols(Locale.US));

		int day = now.get(Calendar.DAY_OF_WEEK);
		int offset = offsets[day - Calendar.SUNDAY] - 1;
		now.add(Calendar.DAY_OF_YEAR, offset);
		String temp = null;

		for (int i = 0; i < weekNum; i++) {
			now.add(Calendar.DAY_OF_YEAR, +1);
			temp = formatter.format(now.getTime());
			now.add(Calendar.DAY_OF_YEAR, +6);
			temp += "-" + formatter.format(now.getTime());
			m.put(new Integer(i), temp);
		}

		return m;
	}

	/**
	 * Get the start date - end date string according to selected base date and week number
	 * The string format is "yyyy/MM/dd - yyyy/MM/dd"
	 * 
	 * @param int weekNum number of weeks contained in result string
	 * @param Date baseDate start date of last week
	 * @return SortedMap
	 */
	public static SortedMap getPreWeeks(int weekNum, Date baseDate) {
		SortedMap m = new TreeMap();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DAY_OF_YEAR, -((7 * (weekNum - 1)) + 1));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", new DateFormatSymbols(Locale.US));
		String temp = "";

		for (int i = 0; i < weekNum; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, +1);
			temp = formatter.format(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, +6);
			temp += " - " + formatter.format(calendar.getTime());
			m.put(new Integer(i), temp);
		}

		return m;
	}

	/**
	 * Get the date before the base date passed in as first parameter and interval is
	 * specified by second parameter
	 * 
	 * @param Date baseDate
	 * @param int days
	 * @return Date
	 */
	public static Date getPreviuosDate(Date baseDate, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		return calendar.getTime();
	}

	public static String checkAndCorrectDateStr(String baseDateStrOld, String standPattern) {

		String baseDateStr = baseDateStrOld.trim();
		if (standPattern.equals(DatetimeUtil.YYYYMMDDHHMM)) {
			System.out.print("baseDateStr.length() + " + baseDateStr.length());
			if (baseDateStr.length() <= 12) {
				StringBuffer sb = new StringBuffer(baseDateStr);
				int differ = 12 - baseDateStr.length();
				while (differ > 0) {
					sb.append("0");
					differ--;

				}
				return sb.toString();
			}
		}

		return null;
	}

	public static String getPreviuosMonth(String baseDateStr, int Month) {

		Date baseDate = DatetimeUtil.stringToDate(baseDateStr, DatetimeUtil.YYYYMM);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.MONTH, -Month);
		Date resultDate = calendar.getTime();

		return DatetimeUtil.dateToString(resultDate, DatetimeUtil.YYYYMM);
	}

	public static Date getNextMonth(Date baseDate, int Month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.MONTH, Month);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static String getPreviuosMonth(Date baseDate, int Month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.MONTH, -Month);
		Date resultDate = calendar.getTime();

		return DatetimeUtil.dateToString(resultDate, DatetimeUtil.YYYYMM);
	}

	public static Date getThisPreviuosMonth(Date baseDate, int Month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.MONTH, -Month);
		return calendar.getTime();

	}

	public static Date getPreviuosDate(String baseDateStr, int day, String partten) {

		Date baseDate = DatetimeUtil.stringToDate(baseDateStr, partten);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DATE, -day);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static Date getPreviuosRoundDate(String baseDateStr, String partten) {

		Date baseDate = DatetimeUtil.stringToDate(baseDateStr, partten);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static Date getPreviuosRoundDate(Date baseDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static Date getPreviuosRoundMonth(Date baseDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static Date getPreviuosHour(String baseDateStr, int hour, String partten) {

		Date baseDate = DatetimeUtil.stringToDate(baseDateStr, partten);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.HOUR, -hour);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	public static Date getPreviuosRoundHour(String baseDateStr, String partten) {

		Date baseDate = DatetimeUtil.stringToDate(baseDateStr, partten);

		if (baseDate == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.set(Calendar.MINUTE, 0);
		Date resultDate = calendar.getTime();

		return resultDate;
	}

	/**
	 * Get the date after the base date passed in as first parameter and interval is
	 * specified by second parameter
	 * 
	 * @param Date baseDate
	 * @param int days
	 * @return Date
	 */
	public static Date getNextDate(Date baseDate, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	/**
	 * Get start date of weeks specified by parameter weekNum beginning from the week
	 * specified by parameter baseDate
	 * 
	 * @param Date baseDate start point of calculation
	 * @param int weekNum number of weeks
	 * @return List a list of start date of weeks
	 */
	public static List getStartDates(Date baseDate, int weekNum) {
		List result = new ArrayList();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		result.add(new Date(baseDate.getTime()));

		for (int i = 0; i < (weekNum - 1); i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			result.add(calendar.getTime());
		}

		return result;
	}

	/**
	 * Get Monday of the week the day specified by parameter belongs to
	 * 
	 * @param Date date any date
	 * @return Date Monday of the week
	 */
	public static Date getStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// get day of week
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		// get offset between current date and Monday of the week
		int offset = offsets[day - Calendar.SUNDAY];

		// adjust to Monday
		calendar.add(Calendar.DAY_OF_YEAR, offset);

		return calendar.getTime();
	}

	public static String getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// get day of week
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		return dayOfWk[day - Calendar.SUNDAY];
	}

	public static boolean isSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	public static boolean isMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}

	public static boolean isTuesday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;
	}

	public static boolean isWednesday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY;
	}

	public static boolean isThursday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
	}

	public static boolean isFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
	}

	public static boolean isSaturday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
	}

	public static int getDayOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
	}

	public static Date truncTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static int compareDay(String timeComparing, String timeCompared, String format) {
		Date timeComparingDate = stringToDate(timeComparing, format);
		Date timeComparedDate = stringToDate(timeCompared, format);

		return compareDay(timeComparingDate, timeComparedDate);
	}

	public static int compareDay(Date first, Date second) {
		Calendar cf = Calendar.getInstance();
		cf.setTime(first);
		Calendar cs = Calendar.getInstance();
		cs.setTime(second);
		int offset = ((cf.get(Calendar.YEAR) - cs.get(Calendar.YEAR)) * 12 * 30)
				+ ((cf.get(Calendar.MONTH) - cs.get(Calendar.MONTH)) * 30)
				+ (cf.get(Calendar.DATE) - cs.get(Calendar.DATE));
		return offset;
	}

	public static int compareMonth(String timeComparing, String timeCompared, String format) {
		Date timeComparingDate = stringToDate(timeComparing, format);
		Date timeComparedDate = stringToDate(timeCompared, format);

		return compareMonth(timeComparingDate, timeComparedDate);
	}

	public static int compareMonth(Date first, Date second) {
		Calendar cf = Calendar.getInstance();
		cf.setTime(first);
		Calendar cs = Calendar.getInstance();
		cs.setTime(second);
		int offset = ((cf.get(Calendar.YEAR) - cs.get(Calendar.YEAR)) * 12)
				+ (cf.get(Calendar.MONTH) - cs.get(Calendar.MONTH));
		return offset;
	}

	public static int getMonthDays(String datecontent, String formate) {
		Date date = DatetimeUtil.stringToDate(datecontent, formate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int rtValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return rtValue;
	}

	public static String getNextNMonth(String datecontent, String formate, int n) {
		Date date = DatetimeUtil.stringToDate(datecontent, formate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		Date resultDate = cal.getTime();
		return DatetimeUtil.dateToString(resultDate, formate);
	}
}
