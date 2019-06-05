package cn.yangqun.common.util;

import java.util.Calendar;

public class DateUtil {
	public static Integer YEAR() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return year ;
		
	}

}
