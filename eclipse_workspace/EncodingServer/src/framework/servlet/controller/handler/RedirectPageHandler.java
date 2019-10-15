package framework.servlet.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.servlet.controller.vo.PageMapperVO;

/**
 * 사용자의 페이지요청 작업을 처리하는 핸들러
 * @author 박유현
 * @since 2019.09.30
 */
public interface RedirectPageHandler {
	/**
	 * page ~
	 * @return 생성되는 URL 주소(사용자가 입력할 URL 주소)
	 */
	public String getURL();
	/**
	 * GET방식 페이지 요청에 대해 필요한 작업을 수행하는 메서드
	 * @param req
	 * @param resp
	 * @return
	 */
	public PageMapperVO doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception;
	/**
	 * POST방식 페이지 요청에 대해 필요한 작업을 수행하는 메서드
	 * @param req
	 * @param resp
	 * @return
	 */
	public PageMapperVO doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
