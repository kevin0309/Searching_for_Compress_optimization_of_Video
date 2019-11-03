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
import framework.servlet.controller.handler.FileUploadHandler;
import framework.util.ClassFounder;
import framework.util.DateUtil;
import framework.util.JSONUtil;
import framework.util.LogUtil;
/**
 * 파일 업로드 요청을 제어(처리X)하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebServlet( name="FileUploadController", urlPatterns= {"/upload/*"}, loadOnStartup=1)
public class FileUploadController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Map<String, FileUploadHandler> handlerMap = new HashMap<>();
	
	/**
	 * 서블릿 초기화에 필요한 기능을 수행하는 메서드
	 */
	public void init() throws ServletException {
		try {
			ClassFounder cf = new ClassFounder(FileUploadHandler.class);
			ArrayList<Class<?>> handlerList = cf.searchHandlerChildren(URLDecoder.decode(getClass().getResource("/").getPath(), "UTF-8"));
			
			for (Class<?> tmp : handlerList) {
				FileUploadHandler tmpHandler = (FileUploadHandler) tmp.newInstance();
				
				if (!tmpHandler.getURL().startsWith("/upload/")) //URL 패턴이 맞는지 검사
					throw new WrongURLException(tmp.getSimpleName()+" - "+tmpHandler.getURL());
				for (Class<?> tmp2 : handlerList) {//중복되는 URL이 있는지 체크
					FileUploadHandler tmp2Handler = (FileUploadHandler) tmp2.newInstance();
					if (!tmp.getName().equals(tmp2.getName()) && tmpHandler.getURL().equals(tmp2Handler.getURL()))
						throw new DuplicateURLException(tmp.getSimpleName()+", "+tmp2.getSimpleName()+" - "+tmpHandler.getURL());
				}
			}
			
			for (Class<?> tmp : handlerList)
			{
				FileUploadHandler tmpHandler = (FileUploadHandler) tmp.newInstance();
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
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String command = req.getRequestURI();
		if (command.indexOf(req.getContextPath()) == 0)
			command = command.substring(req.getContextPath().length());

		FileUploadHandler handler = handlerMap.get(command);
		
		if (handler == null) {
			LogUtil.printErrLog(req.getRemoteAddr(), "undefined URI");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else {
			JSONObject obj = new JSONObject();
			String logMsg = "";
			obj.put("responseDate", DateUtil.getSysdateStr());
			
			try {
				String res = handler.process(req, resp);
				obj.put("resultData", "Upload complete.");
				obj.put("statusMsg", "OK");
				logMsg += "Status : Upload complete";
				if (res != null)
					logMsg += ", ContentType: "+res;
			} catch (Exception e) {
				obj.put("statusMsg", "Server Logic error occured. ("+e.getLocalizedMessage()+")");
				logMsg += "Status : Server Logic error occured. ("+e.getMessage()+")";
				e.printStackTrace();
			}
			resp.setContentType("application/json");
			resp.getWriter().println(JSONUtil.toString_obj(obj));
			resp.getWriter().flush();
			resp.getWriter().close();
			
			LogUtil.printLog(req.getRemoteAddr(), logMsg);
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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
