package framework.servlet.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

/**
 * JSON 형식으로 요청을 처리하는 핸들러
 * @author 박유현
 * @since 2019.09.30
 */
public interface AjaxRequestHandler {
	/**
	 * process ~
	 * @return 생성되는 URL 주소(사용자가 입력할 URL 주소)
	 */
	public String getURL();
	/**
	 * 서블릿 요청에 대해 필요한 작업을 수행하는 메서드
	 * @param req
	 * @param resp
	 * @return JSONAware 로 구성된 객체 혹은 배열
	 * @throws Exception
	 */
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
