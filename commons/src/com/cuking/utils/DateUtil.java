package com.cuking.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public final class DateUtil {

	private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	private final static SimpleDateFormat SIMPLE_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");

	private static final String timeStampFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String dateFormat = "yyyy-MM-dd";
	private static final String monthFormat = "yyyy-MM";
	private static final String hourFormat = "HH:mm:ss";

	public static String toMysqlDateTime(Date date) {
		return SIMPLE_DATE_FORMAT.format(date.getTime());
	}

	public static Date nowDate(){
		return new Date();
	}

    public static String formatNowDate(){
		return SIMPLE_DATE_FORMAT.format(nowDate().getTime());
    }

	public static Date toDate(String s) {
		try {
			return SIMPLE_DATE_FORMAT.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	public static Date toDate2(String s) {
		try {
			return SIMPLE_DATE_FORMAT2.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 日期转化为字符串
	 * @param date
	 * @return
     */
	public static String toDateString(Date date) {
		return SIMPLE_DATE_FORMAT2.format(date);
	}

	public long getDateTime(Date date) {
		return date.getTime();
	}


	public static String remainDays(Date endDate) {
		if(null == endDate) return "0";

		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate());
		long now = cal.getTimeInMillis();
		cal.setTime(endDate);
		long end = cal.getTimeInMillis();

		long diffDays=(end - now)/(1000*3600*24) + 1;

		if(0 > diffDays) {
			diffDays = 0;
		}

		return diffDays + "";
	}

	public static long remainDaysInt(Date endDate) {
		if(null == endDate) return 0;

		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate());
		long now = cal.getTimeInMillis();
		cal.setTime(endDate);
		long end = cal.getTimeInMillis();

		long diffDays=(end - now)/(1000*3600*24) + 1;

		if(0 > diffDays) {
			diffDays = 0;
		}

		return diffDays;
	}

	public static long passHours(Date beginDate) {
		if(null == beginDate) return 0;

		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long begin = cal.getTimeInMillis();
		cal.setTime(nowDate());
		long end = cal.getTimeInMillis();

		long diffHours=(end - begin)/(1000*3600);

		if(0 > diffHours) {
			diffHours = 0;
		}

		return diffHours;
	}

	public static String getTimeStamp(){
		return TIME_STAMP_FORMAT.format(nowDate().getTime());
	}

	public static Date getFormateDateTime(String s) {
		Date date = null;
		try {
			date = TIME_STAMP_FORMAT.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 获取今天剩余毫秒数
	 *
	 * @return
     */
	public static long fetchTodayEndMillis() {
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		long next = cal.getTime().getTime();
		return next - now;
	}

	public static Date formatDateEnd(Date date) {
		try {
			date = SIMPLE_DATE_FORMAT.parse(SIMPLE_DATE_FORMAT2.format(date) + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getBeforeOneDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	public static Date getDayBegin() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return  cal.getTime();
	}

	public static Date getDayEnd() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return  cal.getTime();
	}

	public static Date getBeginDateTime(String beginDate) {
		String s = "/";
		if(beginDate.indexOf(s) != -1){
			beginDate = beginDate.replaceAll(s,"-");
		}
		Date beginDateTime = null;
		try {
			beginDateTime = SIMPLE_DATE_FORMAT.parse(beginDate.substring(0, 10) + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return beginDateTime;
	}

	public static Date getEndDateTime(String endDate) {
		String s = "/";
		if(endDate.indexOf(s) != -1){
			endDate = endDate.replaceAll(s,"-");
			String[] endDateTime = endDate.split("\\-");

			for(int e = 1; e <= 9; ++e) {
				int i = Integer.parseInt(endDateTime[1]);
				if(e == i) {
					endDateTime[1] = "0" + String.valueOf(e);
					break;
				}
			}
			for(int e = 1; e <= 9; ++e) {
				int i = Integer.parseInt(endDateTime[2]);
				if(e == i) {
					endDateTime[2] = "0" + String.valueOf(e);
					break;
				}
			}
			endDate = endDateTime[0] + "-" + endDateTime[1] + "-" + endDateTime[2];
		}
		Date endDateTime = null;
		try {
			endDateTime = SIMPLE_DATE_FORMAT.parse(endDate.substring(0, 10) + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return endDateTime;
	}

	public static int getHourOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static boolean isExpire(Date nowDate, Date oldDate, int cycle) {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(oldDate);
		newCalendar.add(Calendar.DAY_OF_MONTH, cycle);
		newCalendar.set(Calendar.HOUR_OF_DAY, 0);
		newCalendar.set(Calendar.MINUTE, 0);
		newCalendar.set(Calendar.SECOND, 0);

		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);

		if(nowCalendar.getTimeInMillis() >= newCalendar.getTimeInMillis()) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isCurrentDate(Date nowDate, Date oldDate) {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(oldDate);
		newCalendar.set(Calendar.HOUR_OF_DAY, 0);
		newCalendar.set(Calendar.MINUTE, 0);
		newCalendar.set(Calendar.SECOND, 0);

		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		nowCalendar.set(Calendar.MINUTE, 0);
		nowCalendar.set(Calendar.SECOND, 0);

		if (nowCalendar.getTimeInMillis() == newCalendar.getTimeInMillis()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 获取当前时间串
	 * @return
     */
	public static String getCurrentTimeS(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 获取小时差
	 * @param start
	 * @param end
     * @return
     */
	public static long diffByHours(Date start, Date end){
		long total_minute = (end.getTime() - start.getTime())/(1000*60);
		return total_minute/60;

	}


	/**
	 * 解析 date字符串
	 * @param dateS
	 * @param patten
     * @return
     */
	public static Date paseString(String dateS, String patten){
		SimpleDateFormat sdf = new SimpleDateFormat(patten);

		try {
			return sdf.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 格式化日期
	 * @param dateS
	 * @param patten
	 * @return
	 */
	public static String forMatDate(Date dateS, String patten){
		if (patten == null) {
			patten = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		try {
			return sdf.format(dateS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 增加 或者减少 天数
	 * @param date
	 * @param val
     * @return
     */
	public static Date addDays(Date date, int val ){
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, val);
		return cal.getTime();
	}

	public static Date addMinute(Date date, int val ){
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.add(Calendar.MINUTE, val);
		return cal.getTime();
	}

	/**
	 * 获取今天剩余毫秒数
	 *
	 * @return
	 */
	public static long fetchEndMillis() {
		long now = System.currentTimeMillis();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		if(2 < cal.get(Calendar.HOUR_OF_DAY)) {
			cal.set(Calendar.HOUR_OF_DAY, 2);
			cal.set(Calendar.MINUTE, 50);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		} else {
			cal.set(Calendar.HOUR_OF_DAY, 2);
			cal.set(Calendar.MINUTE, 50);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}

		long next = cal.getTimeInMillis();

		return next - now;
	}
	public static String forMatHours(long hours){
		return hours/24+"天"+hours%24+"小时";
	}


	/**
	 * 得到常用的日期(保证月末对月末，例如，3.31的前一个月对应日期为2.29或2.28)
	 * @param date		指定日期
	 * @param years		指定相差年数（正数为向后，负数为向前）
	 * @param months	指定相差月数（正数为向后，负数为向前）
	 * @param days		指定相差天数（正数为向后，负数为向前）
	 */
	private static Date getNormalDate(Date date, int years, int months, int days)
	{
		if (date == null)
		{
			return null;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		calendar.add(Calendar.MONTH, months);
		calendar.add(Calendar.DATE, days);

		return new Date(calendar.getTime().getTime());
	}

	/**
	 * 得到常用的日期(保证月末对月末，例如，3.31的前一个月对应日期为2.29或2.28)
	 * @param timastamp	指定日期
	 * @param years		指定相差年数（正数为向后，负数为向前）
	 * @param months	指定相差月数（正数为向后，负数为向前）
	 * @param days		指定相差天数（正数为向后，负数为向前）
	 */
	private static Timestamp getNormalTimestamp(Timestamp timastamp, int years, int months, int days)
	{
		if (timastamp == null)
		{
			return null;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(timastamp);
		calendar.add(Calendar.YEAR, years);
		calendar.add(Calendar.MONTH, months);
		calendar.add(Calendar.DATE, days);

		return new Timestamp(calendar.getTime().getTime());
	}

	/** 获得该日期指定天数之前的日期
	 * @param date 		指定日期
	 * @param days		指定天数
	 * @return 返回日期
	 */
	public static Date before(Date date, int days)
	{
		return getNormalDate(date, 0, 0, 0-days);
	}

	/** 获得该日期指定天数之后的日期
	 * @param date		指定日期
	 * @param days		指定天数
	 * @return 返回日期
	 */
	public static Date after(Date date, int days)
	{
		return getNormalDate(date, 0, 0, days);
	}

	/** 获得该日期指定天数之前的日期
	 * @param timestamp	指定日期
	 * @param days		指定天数
	 * @return 返回日期
	 */
	public static Timestamp before(Timestamp timestamp, int days)
	{
		return getNormalTimestamp(timestamp, 0, 0, 0 - days);
	}

	/** 获得该日期指定天数之后的日期
	 * @param timestamp	指定日期
	 * @param days		指定天数
	 * @return 返回日期
	 */
	public static Timestamp after(Timestamp timestamp, int days)
	{
		return getNormalTimestamp(timestamp, 0, 0, days);
	}

	/**
	 * 获得两个日期之间有多少天
	 * @param beginDate	开始日期
	 * @param endDate	截止日期
	 * @return 两日期的间隔天数
	 */
	public static long getIntervalDays(Date beginDate, Date endDate)
	{
		GregorianCalendar gc1, gc2;

		gc1 = new GregorianCalendar();
		gc1.setTime(beginDate);
		gc2 = new GregorianCalendar();
		gc2.setTime(endDate);


		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);

		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);

		long lInterval = 0;
		lInterval = gc2.getTime().getTime() - gc1.getTime().getTime();
		lInterval = lInterval / (24 * 60 * 60 * 1000);
		return lInterval;
	}

	/**
	 * 获得两个日期之间有多少天
	 * @param beginTimestamp	开始日期
	 * @param endTimestamp		截止日期
	 * @return	两日期的间隔天数
	 */
	public static long getIntervalDays(Timestamp beginTimestamp, Timestamp endTimestamp)
	{
		long lInterval = -1;
		Calendar calBegin = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calBegin.setTime(new Date(beginTimestamp.getTime()));
		calBegin.set(Calendar.HOUR_OF_DAY, 0);
		calBegin.set(Calendar.MINUTE, 0);
		calBegin.set(Calendar.SECOND, 0);
		calEnd.setTime(new Date(endTimestamp.getTime()));
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);

		Timestamp begin = new Timestamp(calBegin.getTimeInMillis());
		Timestamp end = new Timestamp(calEnd.getTimeInMillis());
		lInterval = end.getTime() - begin.getTime();
		lInterval = lInterval / (24 * 60 * 60 * 1000);

		return lInterval;
	}

	/**
	 * 比较两个日期是否相等，按照年，月，日比较。
	 * @param firstDate		第一个日期
	 * @param secondDate	第二个日期
	 * @return 	 0：两个日期相等;
	 * 			 1：第一个日期大于第二个日期（即第一个日期要晚于第二个日期）
	 * 			-1：第一个日期小于第二个日期（即第一个日期要早于第二个日期）
	 */
	public static int compareDate(Date firstDate, Date secondDate)
	{
		int result = 0;

		if (firstDate != null && secondDate != null)
		{
			Calendar calOne = Calendar.getInstance();
			calOne.clear();
			calOne.setTime(firstDate);

			Calendar calOther = Calendar.getInstance();
			calOther.clear();
			calOther.setTime(secondDate);

			if (calOne.get(Calendar.YEAR) == calOther.get(Calendar.YEAR))
			{
				if (calOne.get(Calendar.MONTH) == calOther.get(Calendar.MONTH))
				{
					if (calOne.get(Calendar.DATE) == calOther.get(Calendar.DATE))
					{
						//日期相等
						result = 0;
					}
					else if (calOne.get(Calendar.DATE) > calOther.get(Calendar.DATE))
					{
						//第一个晚于第二个
						result = 1;
					}
					else if (calOne.get(Calendar.DATE) < calOther.get(Calendar.DATE))
					{
						//第一个早于第二个
						result = -1;
					}
				}
				else if (calOne.get(Calendar.MONTH) > calOther.get(Calendar.MONTH))
				{
					//第一个晚于第二个
					result = 1;
				}
				else if (calOne.get(Calendar.MONTH) < calOther.get(Calendar.MONTH))
				{
					//第一个早于第二个
					result = -1;
				}
			}
			else if (calOne.get(Calendar.YEAR) > calOther.get(Calendar.YEAR))
			{
				//第一个晚于第二个
				result = 1;
			}
			else if (calOne.get(Calendar.YEAR) < calOther.get(Calendar.YEAR))
			{
				//第一个早于第二个
				result = -1;
			}

		}

		return result;
	}

	/**
	 * 根据指定的年份和月份获得该月的天数（二月会根据是否闰年获取其真实天数）。
	 * @param year		指定年份
	 * @param month		指定月份
	 * @return 该月的总天数
	 */
	public static int getDaysOfMonth(int year, int month)
	{
		int nDay = Integer.parseInt(getDaysPerMonth().get(String.valueOf(month)).toString());
		if (month == 2)
		{
			if ((year % 4 == 0) && (year % 100 > 0)) // 闰年
				nDay = 29;
		}
		return nDay;
	}

	/**
	 * 获得给定月份的天数（二月统一按照28天算）
	 * @return
	 */
	private static HashMap<String, String> getDaysPerMonth()
	{
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("1", "31");
		hm.put("2", "28");
		hm.put("3", "31");
		hm.put("4", "30");
		hm.put("5", "31");
		hm.put("6", "30");
		hm.put("7", "31");
		hm.put("8", "31");
		hm.put("9", "30");
		hm.put("10", "31");
		hm.put("11", "30");
		hm.put("12", "31");
		return hm;
	}

	/**
	 * 获取Timastamp型的当前时间（服务器时间）。
	 * @return 当前时间
	 */
	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取java.sql.Date型的当前日期（服务器日期）。
	 * @return 当前日期
	 */
	public static java.sql.Date getCurrentSqlDate()
	{
		return new java.sql.Date( (new Date()).getTime() );
	}

	/**
	 * 获取java.sql.Date型的当前日期（服务器日期）。
	 * @return 当前日期
	 */
	public static Date getCurrentUtilDate()
	{
		return new Date();
	}

	/**
	 * 字符串转换为Timestamp类型。
	 * @param 	:yyyy-MM-dd
	 * @return Timestamp
	 * @throws ParseException
	 */
	public static Timestamp stringTimestamp(String datePattern) throws ParseException
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(format.parse(datePattern).getTime());
		return ts;
	}

	/**
	 * 字符串转换为Timestamp类型。
	 * @param 	：yyyy-MM-dd
	 * @return Timestamp
	 * @throws ParseException
	 */
	public static Timestamp stringTimestamp2(String datePattern) throws ParseException
	{
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Timestamp ts = new Timestamp(format.parse(datePattern).getTime());
		return ts;
	}
	/**
	 * 根据指定的格式化参数将日期格式化成相应的字符串。
	 * @param date			指定日期(java.util.Date或者java.sql.Date)
	 * @param datePattern	格式化模式（例如：yyyy-MM-dd; yyyy/MM/dd; yyyy-MM-dd HH:mm:ss 等等）
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date, String datePattern)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		return formatter.format(date);
	}

	/**
	 * 根据指定的格式化参数将日期格式化成相应的字符串。
	 * @param timestamp			指定日期(java.sql.Timestamp)
	 * @param datePattern	格式化模式（例如：yyyy-MM-dd; yyyy-MM-dd HH:mm:ss; yyyy-MM-dd HH:mm:ss.SSS 等等）
	 * @return 格式化后的字符串
	 */
	public static String formatTimestamp(Timestamp timestamp, String datePattern)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		return formatter.format(timestamp);
	}

	/**
	 * 返回 2015-01-01 11:11:11
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(Timestamp timestamp)
	{
		return SIMPLE_DATE_FORMAT.format(timestamp);
	}

	/**
	 * 返回 yyyy-mm-dd
	 * @param
	 * @return
	 */
	private static String formatTimestamp2(Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(timestamp);
	}

	/**
	 * 将Date日期格式化成中文日期字符串（XXXX年XX月XX日）。
	 * @param date		指定日期(java.util.Date或者java.sql.Date)
	 * @return 格式化后的字符串
	 */
	public static String formatChineseDate(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String strMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (strMonth.length() == 1)
		{
			strMonth = "0" + strMonth;
		}
		String strDay = String.valueOf(calendar.get(Calendar.DATE));
		if (strDay.length() == 1)
		{
			strDay = "0" + strDay;
		}

		return calendar.get(Calendar.YEAR) + "年" + strMonth + "月" + strDay + "日";
	}

	/**
	 * 将Timestamp日期格式化成中文日期字符串（XXXX年XX月XX日）。
	 * @param timestamp		指定日期(java.sql.Timestamp)
	 * @return 格式化后的字符串
	 */
	public static String formatChineseTimestamp(Timestamp timestamp)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		String strMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (strMonth.length() == 1)
		{
			strMonth = "0" + strMonth;
		}
		String strDay = String.valueOf(calendar.get(Calendar.DATE));
		if (strDay.length() == 1)
		{
			strDay = "0" + strDay;
		}

		return calendar.get(Calendar.YEAR) + "年" + strMonth + "月" + strDay + "日";
	}

	public static String formatCurrentTime(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String strMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String strDay = String.valueOf(calendar.get(Calendar.DATE));
		return calendar.get(Calendar.YEAR) + strMonth + strDay;
	}

	/**
	 * Date的String类型转化为 Timestamp 类型
	 * @param dateString
	 * @param datePattern
	 * @return
	 * @throws Exception
	 */
	public static Timestamp formatStringToTimestamp(String dateString, String datePattern) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		return new Timestamp(formatter.parse(dateString).getTime());
	}

	/**
	 * 获取当前系统时间
	 * 返回 yyyy-mm-dd hh:mm:ss
	 * @return
	 */
	public static String getCurrentDate1() {
		return formatDate(new Date(), timeStampFormat);
	}

	/**
	 * 获取固定日期格式化后的时间
	 * 返回 yyyy-mm-dd hh:mm:ss
	 * @return
	 */
	public static String getFormatDate1(Date date) {
		if(null == date) return "";
		return formatDate(date, timeStampFormat);
	}



	/**
	 * 获取当前系统时间
	 * 返回 yyyy-mm-dd
	 * @return
	 */
	public static String getDateStr(Date date) {
		return formatDate(date, dateFormat);
	}

	/**
	 * 获取固定日期格式化后的时间
	 * 返回 yyyy-mm-dd
	 * @return
	 */
	public static String getFormatDate2(Date date) {
		return formatDate(date, dateFormat);
	}


	/**
	 * 获取固定日期格式化后的时间
	 * @param date
	 * @return yyyy-mm
	 */
	public static String getFormatMonth(Date date) {
		return formatDate(date, monthFormat);
	}

	public static String getFormatHour(Date date) {
		return formatDate(date, hourFormat);
	}

	/**
	 * 返回yyyy-mm-dd
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String getSummaryDataString(String dateString) throws Exception {
		if(dateString == null || dateString.equals("")) {
			return "";
		}
		return formatTimestamp2(formatStringToTimestamp(dateString, dateFormat));
	}

	/***
	 * yyyy-mm-dd  hh:mm:ss
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String getDataString(String dateString) throws Exception {
		if(dateString == null || dateString.equals("")) {
			return "";
		}
		return formatTimestamp(formatStringToTimestamp(dateString, timeStampFormat));
	}

	/**
	 * 获取本周一时间
	 * @return
	 */
	public static Date getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime();
	}
	/**
	 * 获取本周一时间的凌晨
	 * @return
	 */
	public static Date getMondayOfThisWeekStart() {
		Date date = getMondayOfThisWeek();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
		cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
		return  cal.getTime();
	}

	/**
	 * 获取days天之前的凌晨
	 * @return
	 */
	public static Date getAnyDaysBeforeStart(int days) {
		Date date = before(new Date(),days);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
		cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
		return  cal.getTime();
	}

	public static String getSimpleDate(Date date) {

		String format = "yyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 解析字符串
	 * @param dateS
	 * @param format
	 * @return
	 */
	public static Date parseDataS(String dateS, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = null;
		try {
			date = sdf.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 根据过期时间获取到期天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long diffDays(Date start, Date end){

		long times =end.getTime() - start.getTime();
		long day = 1000 * 60 * 60 * 24;

		return times%day == 0?times/day:(times/day)+1;
	}

	/**
	 * 获取当天的剩余时间
	 * @return
	 */
	public static long getTodayDieSecond(){
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate
				.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
				.get(Calendar.DATE) + 1, 0, 0, 0);
		return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
	}

	public static Date addHours(Date date,int val ){
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, val);
		return cal.getTime();
	}

	/**
	 * 格式化过期时间
	 * @param date
	 * @return
	 */
	public static Date todayDieTime(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.set(11,23);
		cal.set(12,59);
		cal.set(13,59);
		return cal.getTime();
	}



	/**
	 * 获取当天的开始时间
	 * @return
     */
	public static Date getStartTime() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	//

	/**
	 * 获取当天的结束时间
	 * @return
     */
	public static Date getEndTime() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	public static void main(String[] args) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(sdf.format(getStartTime()));
//		System.out.println(sdf.format(getEndTime()));


	}

}
