package framework.servlet.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.init.ServerConfig;
import framework.servlet.controller.exceptions.DuplicateURLException;
import framework.servlet.controller.exceptions.WrongURLException;
import framework.servlet.controller.handler.RedirectPageHandler;
import framework.servlet.controller.vo.PageMapperVO;
import framework.util.ClassFounder;
import framework.util.LogUtil;

/**
 * 페이지 요청을 처리하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebServlet( name="RedirectPageController", urlPatterns= {"/page/*"}, loadOnStartup=1)
public class RedirectPageController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Map<String, RedirectPageHandler> handlerMap = new HashMap<>();
	
	/**
	 * 서블릿 초기화에 필요한 기능을 수행하는 메서드
	 */
	public void init() throws ServletException {
		try {
			ClassFounder cf = new ClassFounder(RedirectPageHandler.class);
			ArrayList<Class<?>> handlerList = cf.searchHandlerChildren(URLDecoder.decode(getClass().getResource("/").getPath(), "UTF-8"));
			
			for (Class<?> tmp : handlerList) {
				RedirectPageHandler tmpHandler = (RedirectPageHandler) tmp.newInstance();
				
				if (!tmpHandler.getURL().startsWith("/page/")) //URL 패턴이 맞는지 검사
					throw new WrongURLException(tmp.getSimpleName()+" - "+tmpHandler.getURL());
				for (Class<?> tmp2 : handlerList) {//중복되는 URL이 있는지 체크
					RedirectPageHandler tmp2Handler = (RedirectPageHandler) tmp2.newInstance();
					if (!tmp.getName().equals(tmp2.getName()) && tmpHandler.getURL().equals(tmp2Handler.getURL()))
						throw new DuplicateURLException(tmp.getSimpleName()+", "+tmp2.getSimpleName()+" - "+tmpHandler.getURL());
				}
			}
			
			for (Class<?> tmp : handlerList)
			{
				RedirectPageHandler tmpHandler = (RedirectPageHandler) tmp.newInstance();
				handlerMap.put(tmpHandler.getURL(), tmpHandler);
			}
		}
		catch (WrongURLException e) {
			LogUtil.printErrLog("init error wrong URL detected : "+e.getLocalizedMessage());
			throw new ServletException(e);
		}
		catch (DuplicateURLException e) {
			LogUtil.printErrLog("init error duplicate URL detected : "+e.getLocalizedMessage());
			throw new ServletException(e);
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}
	/**
	 * 서블릿 요청을 처리하는 메서드
	 * @param req
	 * @param resp
	 * @param isGet
	 * @throws ServletException
	 * @throws IOException
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp, boolean isGet) throws ServletException, IOException {
		String command = req.getRequestURI();
		if (command.indexOf(req.getContextPath()) == 0)
			command = command.substring(req.getContextPath().length());
		
		RedirectPageHandler handler = handlerMap.get(command);
		
		if (handler == null) {
			LogUtil.printErrLog(req.getRemoteAddr(), "undefined URI");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else {
			PageMapperVO mapper = null;
			
			try {
				if(isGet) 
					mapper = handler.doGet(req, resp);
				else
					mapper = handler.doPost(req, resp);
			} catch (Exception e) {
				LogUtil.printLog(req.getRemoteAddr(), "Status : Server Logic error occured. ("+e.getMessage()+")");
				e.printStackTrace();
				throw new ServletException(e);
			}
			
			if(mapper == null) {
				LogUtil.printErrLog("handler Returned Null Object");
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			String viewPageURL;
			viewPageURL = mapper.getViewPageURL();
			
			if(mapper.isRedirect()) {
				resp.sendRedirect(ServerConfig.getServiceContainerName() + viewPageURL);
				LogUtil.printLog(req.getRemoteAddr(), "Redirect to ["+command+"].");
			}
			else {
				RequestDispatcher dispatcher = req.getRequestDispatcher(viewPageURL);
				dispatcher.forward(req, resp);
				LogUtil.printLog(req.getRemoteAddr(), "Forward to ["+command+"].");				
			}
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, true);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, false);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);;
	}
	@Override
	protected void doPut(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}	
}
