package framework.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * JSON 객체를 핸들링할 때 필요한 기능들을 모아둔 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class JSONUtil {

	/**
	 * JSON을 받아 JSONObject로 변환
	 * @param str
	 * @return 변환되어 구현된 JSONObject
	 */
	public static JSONObject convertStringToJSONObject(String str) {
		JSONParser parser = new JSONParser();
		JSONObject result = null;
		
		try {
			result = (JSONObject) parser.parse(str.trim());
		} catch (ParseException e) {
			LogUtil.printErrLog("String format error");
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/**
	 * JSON을 받아 JSONArray로 변환
	 * @param str
	 * @return 변환되어 구현된 JSONArray
	 */
	public static JSONArray convertStringToJSONArray(String str) {
		JSONParser parser = new JSONParser();
		JSONArray result = null;
		
		try {
			result = (JSONArray) parser.parse(str);
		} catch (ParseException e) {
			LogUtil.printErrLog("String format error");
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/**
	 * Object의 필드를 Map<String, Object> 로 변환
	 * @param obj
	 * @return 변환되어 구현된 Map<String, Object>
	 */
	public static Map<String, Object> convertObjectToMap(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Map<String, Object> result = new HashMap<>();
			
			for (Field field : fields) {
				field.setAccessible(true);
				result.put(field.getName(), field.get(obj));
			}
			
			return result;
		} catch (IllegalArgumentException e) {
			LogUtil.printErrLog("convert to Map argument error");
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			LogUtil.printErrLog("convert to Map illegal access");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Object를 JSONObject로 변환
	 * @param obj
	 * @return 변환되어 구현된 JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject convertObjectToJSONObject(Object obj) {
		try {
			JSONObject res = new JSONObject();
			res.putAll(convertObjectToMap(obj));
			return res;
		} catch (Exception e) {
			LogUtil.printErrLog("unexpected error");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ArrayList를 JSONArray로 변환
	 * ArrayList의 프로퍼티도 모두 JSONParser에서 변환가능한 형태여야함
	 * @param arr
	 * @return 변환되어 구현된 JSONArray
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray convertArrayListToJSONArray(ArrayList<Object> arr) {
		JSONArray result = new JSONArray();
		for (Object obj : arr)
			result.add(convertObjectToJSONObject(obj));
		return result;
	}
	
	/**
	 * 디폴트값이 []로 정해진 toString
	 * @param arr
	 * @return
	 */
	public static String toString_arr(JSONArray arr) {
		String result = null;
		if (arr == null || arr.size() == 0)
			result = "[]";
		else
			result = arr.toJSONString();
		return result;
	}
	
	/**
	 * 디폴트값이 {}로 정해진 toString
	 * @param obj
	 * @return
	 */
	public static String toString_obj(JSONObject obj) {
		String result = null;
		if (obj == null)
			result = "{}";
		else
			result = obj.toJSONString();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static void setJSONResponse(JSONAware data, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("responseDate", DateUtil.getSysdateStr());
		
		try {
			obj.put("resultData", data);
			obj.put("statusMsg", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("resultData", "");
			obj.put("statusMsg", "Server Logic error occured. ("+e.getLocalizedMessage()+")");
		}
		resp.setContentType("application/json");
		resp.getWriter().println(JSONUtil.toString_obj(obj));
		resp.getWriter().flush();	
		resp.getWriter().close();
	}
}
