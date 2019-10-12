package framework.util;

public class ByteUtil {

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] res = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			res[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		return res;
	}
	
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes)
			sb.append(String.format("%02X", b&0xff));
		return sb.toString();
	}
}
