package encoding.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.servlet.controller.handler.RedirectPageHandler;
import framework.servlet.controller.vo.PageMapperVO;

public class GetEncodingServerListPageAction implements RedirectPageHandler {
	
	private static final String VIEW_PAGE = "/WEB-INF/view/Encoding/server-list.jsp";
	
	@Override
	public String getURL() {
		return "/page/encoding/server-list";
	}

	@Override
	public PageMapperVO doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return new PageMapperVO(VIEW_PAGE);
	}

	@Override
	public PageMapperVO doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return new PageMapperVO(VIEW_PAGE);
	}

}
