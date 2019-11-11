package framework.servlet.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import framework.servlet.controller.exceptions.DuplicateURLException;
import framework.servlet.controller.exceptions.WrongURLException;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.ClassFounder;
import framework.util.DateUtil;
import framework.util.JSONUtil;
import framework.util.LogUtil;

/**
 * 비동기요청을 제어(처리X)하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebServlet( name="AjaxRequestController", urlPatterns= {"/process/*"}, loadOnStartup=1)
public class AjaxRequestController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Map<String, AjaxRequestHandler> handlerMap = new HashMap<>();
	
	/**
	 * 서블릿 초기화에 필요한 기능을 수행하는 메서드
	 */
	public void init() throws ServletException {
		try {
			ClassFounder cf = new ClassFounder(AjaxRequestHandler.class);
			ArrayList<Class<?>> handlerList = cf.searchHandlerChildren(URLDecoder.decode(getClass().getResource("/").getPath(), "UTF-8"));
			
			for (Class<?> tmp : handlerList) {
				AjaxRequestHandler tmpHandler = (AjaxRequestHandler) tmp.newInstance();
				
				if (!tmpHandler.getURL().startsWith("/process/")) //URL 패턴이 맞는지 검사
					throw new WrongURLException(tmp.getSimpleName()+" - "+tmpHandler.getURL());
				for (Class<?> tmp2 : handlerList) {//중복되는 URL이 있는지 체크
					AjaxRequestHandler tmp2Handler = (AjaxRequestHandler) tmp2.newInstance();
					if (!tmp.getName().equals(tmp2.getName()) && tmpHandler.getURL().equals(tmp2Handler.getURL()))
						throw new DuplicateURLException(tmp.getSimpleName()+", "+tmp2.getSimpleName()+" - "+tmpHandler.getURL());
				}
			}
			
			for (Class<?> tmp : handlerList)
			{
				AjaxRequestHandler tmpHandler = (AjaxRequestHandler) tmp.newInstance();
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
	 * 서블릿 요청을 맵핑하는 메서드
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//컨트롤러에 목적에 맞게 페이지 이동에 대한 내용을 담을 것
		String command = req.getRequestURI();
		if (command.indexOf(req.getContextPath()) == 0)
			command = command.substring(req.getContextPath().length());
		
		AjaxRequestHandler handler = handlerMap.get(command);
		
		if (handler == null) {
			LogUtil.printErrLog(req.getRemoteAddr(), "undefined URI");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else { //json 생성해서 등록해주는 것. 형식에 맞춰서 json 생성. 
			JSONObject obj = new JSONObject();
			String logMsg = "["+command+"] ";
			obj.put("responseDate", DateUtil.getSysdateStr());
			
			try {
				obj.put("resultData", handler.process(req, resp));
				obj.put("statusMsg", "OK");
				logMsg += "Status : OK";
			} catch (Exception e) {
				e.printStackTrace();
				obj.put("resultData", "");
				obj.put("statusMsg", "Server Logic error occured. ("+e.getLocalizedMessage()+")");
				logMsg += "Status : Server Logic error occured. ("+e.getMessage()+")";
				e.printStackTrace();
			}
			resp.setContentType("application/json");
			resp.getWriter().println(JSONUtil.toString_obj(obj));
			resp.getWriter().flush();	
			resp.getWriter().close();
			
			logMsg += ", ContentType: application/json";
					
			LogUtil.printLog(req.getRemoteAddr(), logMsg);
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
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
