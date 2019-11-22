package framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import framework.init.ServerConfig;

/**
 * 날짜 관련 유틸을 제공하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat(ServerConfig.getDateFormat());
	
	/**
	 * 현재 시간을 받아오는 메서드
	 * @return sysdate String
	 */
	public static String getSysdateStr() {
		return sdf.format(new Date());
	}
	/**
	 * 날짜를 받아서 String 형식으로 변환해주는 메서드
	 * @param date
	 * @return dateformat String
	 */
	public static String toString(Date date) {
		if (date != null)
			return sdf.format(date);
		else
			return "null";
	}
	
	/**
	 * 날짜형식의 문자열을 받아서, Date로 바꾸는 메서드
	 * @param dateformat String
	 * @return Date
	 */
	public static Date toDate(String str) {
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException("DateUtil parse error", e);
		}
	}
}
