package org.zero.commons.core.support.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZDateUtil {

	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static String DEFAULT_DELEMETER= "-";
	public static String DEFAULT_DELEMETER_PATTERN= "[\\-\\\\\\./]";
	public static long getStringToDate(String strDate)
	{
		return getStringToDate(strDate,DEFAULT_DATE_FORMAT);	
	}
	public static long getStringToDate(String strDate,String dateFormat)  {
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		//Matcher matcher = DELEMETER_PATTERN.matcher(strDate);
		//strDate = matcher.replaceAll("-");
		strDate = strDate.replaceAll(DEFAULT_DELEMETER_PATTERN,DEFAULT_DELEMETER);
	    Date date = null;
		try {
			date = formatter.parse(strDate);
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("DateString parse Exception",e);
		}
	    long dateInLong = date.getTime();
	    return dateInLong;
	}
}
