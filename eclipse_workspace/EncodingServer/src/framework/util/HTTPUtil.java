package framework.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP 요청을 만들어 보내는 유틸 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class HTTPUtil {

	/**
	 * HTTP 요청을 만들어 보냄
	 * @param url
	 * @param method
	 * @param paramStr
	 * @throws IOException
	 */
	public static void sendHttpRequest(String url, String method, String paramStr) throws IOException {
		URL urlObj = new URL(url);
		byte[] postDataBytes = paramStr.getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
	    conn.setDoOutput(true);
	    conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod(method);
		conn.setRequestProperty("User-Agent", "Chrome/version");
	    conn.setRequestProperty("Accept-Charset", "UTF-8");
	    conn.setRequestProperty("charset", "UTF-8");
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setUseCaches(false);
	    if (!paramStr.isEmpty() && paramStr != null) {
		    conn.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));
		    try( DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	    	   wr.write(postDataBytes);
		    }
	    }
		conn.getOutputStream().flush();
		conn.getOutputStream().close();
		conn.getResponseCode();
	}
}
