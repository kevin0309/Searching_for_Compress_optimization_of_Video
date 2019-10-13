package framework.util;

/**
 * byte array를 쉽게 사용하기 위한 유틸 클래스
 * @author 박유현
 * @since 2019.10.12
 */
public class ByteUtil {

	/**
	 * 16진수 형식의 문자열을 받아 byte array로 변환
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] res = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			res[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		return res;
	}
	
	/**
	 * byte array를 16진수 형식의 문자열로 변환
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes)
			sb.append(String.format("%02X", b&0xff));
		return sb.toString();
	}
}
