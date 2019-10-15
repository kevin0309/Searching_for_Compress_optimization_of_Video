package framework.servlet.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 사용자의 파일업로드 요청을 처리하는 핸들러
 * @author 박유현
 * @since 2019.09.30
 */
public interface FileUploadHandler {
	/**
	 * upload ~
	 * @return 생성되는 URL 주소(사용자가 입력할 URL 주소)
	 */
	public String getURL();
	/**
	 * 서블릿 요청에 대해 필요한 작업을 수행하는 메서드
	 * @param req
	 * @param resp
	 * @return 업로드한 파일의 MIME type
	 * @throws Exception
	 */
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
