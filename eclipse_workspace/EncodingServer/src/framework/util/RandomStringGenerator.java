package framework.util;

import java.util.Random;

/**
 * 무작위문자열을 만들어내는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class RandomStringGenerator {
	public static final char[] URL_SAFE_CHAR = new char[] {'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g',
			'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o',
			'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v', 'W', 'w',
			'X', 'x', 'Y', 'y', 'Z', 'z', '-', '_'};
	
	/**
	 * 입력받은 길이만큼의 무작위 문자를 조합하여 문자열생성 (HTTP 요청에서 URL에 사용할 수 있는 문자 사용) 
	 * @param length
	 * @return
	 */
	public static String getRandomURLSafeString(int length) {
		String res = "";
		for (int i = 0; i < length; i++)
			res += URL_SAFE_CHAR[new Random().nextInt(URL_SAFE_CHAR.length)];
		return res;
	}
	
}
