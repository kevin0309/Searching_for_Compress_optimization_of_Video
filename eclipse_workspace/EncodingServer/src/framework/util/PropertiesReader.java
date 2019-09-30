package framework.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
/**
 * java.util.Properties 파일을 읽는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class PropertiesReader {
	/**
	 * property 형식의 문자열을 받아 Properties 객체로 변환
	 * @param str properties형식의 문자열
	 * @return properties
	 */
	public static Properties readPropString(String str) {
		Properties prop = new Properties();
		try {
			prop.load(new StringReader(str));
		} catch (IOException e) {
			throw new RuntimeException("fail to load Properties String", e);
		}		
		return prop;
	}
	/**
	 * 해당 경로의 .properties파일을 읽어 Properties 객체 반환
	 * @param filePath properties파일의 경로
	 * @return properties
	 */
	public static Properties readPropertiesFile(String filePath) {
		Properties prop = new Properties();
		try {
			FileReader fr = new FileReader(filePath);
			prop.load(fr);
		} catch (IOException e) {
			throw new RuntimeException("fail to load Properties File", e);
		}		
		return prop;
	}
	
	/**
	 * properties에서 정수값을 가져오기위해 구현한 기능
	 * @param prop
	 * @param key
	 * @param def 에러 시 디폴트값
	 * @return
	 */
	public static int getIntProperty(Properties prop, String key, int def) {
		String value = prop.getProperty(key);
		if (value == null)
			return def;
		return Integer.parseInt(value);
	}
}
