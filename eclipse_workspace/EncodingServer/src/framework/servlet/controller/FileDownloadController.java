package framework.servlet.controller;

import java.io.File;
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

import org.apache.catalina.connector.ClientAbortException;

import framework.servlet.controller.exceptions.DuplicateURLException;
import framework.servlet.controller.exceptions.WrongURLException;
import framework.servlet.controller.handler.FileDownloadHandler;
import framework.util.ClassFounder;
import framework.util.LogUtil;

/**
 * 파일 다운로드 요청을 제어(처리X)하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebServlet( name="FileDownloadController", urlPatterns= {"/download/*"}, loadOnStartup=1)
public class FileDownloadController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Map<String, FileDownloadHandler> handlerMap = new HashMap<>();
	
	/**
	 * 서블릿 초기화에 필요한 기능을 수행하는 메서드
	 */
	public void init() throws ServletException {
		try {
			ClassFounder cf = new ClassFounder(FileDownloadHandler.class);
			ArrayList<Class<?>> handlerList = cf.searchHandlerChildren(URLDecoder.decode(getClass().getResource("/").getPath(), "UTF-8"));
			
			for (Class<?> tmp : handlerList) {
				FileDownloadHandler tmpHandler = (FileDownloadHandler) tmp.newInstance();
				
				if (!tmpHandler.getURL().startsWith("/download/")) //URL 패턴이 맞는지 검사
					throw new WrongURLException(tmp.getSimpleName()+" - "+tmpHandler.getURL());
				for (Class<?> tmp2 : handlerList) {//중복되는 URL이 있는지 체크
					FileDownloadHandler tmp2Handler = (FileDownloadHandler) tmp2.newInstance();
					if (!tmp.getName().equals(tmp2.getName()) && tmpHandler.getURL().equals(tmp2Handler.getURL()))
						throw new DuplicateURLException(tmp.getSimpleName()+", "+tmp2.getSimpleName()+" - "+tmpHandler.getURL());
				}
			}
			
			for (Class<?> tmp : handlerList)
			{
				FileDownloadHandler tmpHandler = (FileDownloadHandler) tmp.newInstance();
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
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//컨트롤러에 목적에 맞게 페이지 이동에 대한 내용을 담을 것
		String command = req.getRequestURI();
		if (command.indexOf(req.getContextPath()) == 0)
			command = command.substring(req.getContextPath().length());
		
		FileDownloadHandler handler = handlerMap.get(command);
		
		if (handler == null) {
			LogUtil.printErrLog(req.getRemoteAddr(), "undefined URI");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else {
			try {
				File file = handler.process(req, resp);
				//용량 지정
				resp.addHeader("Content-Length", Long.toString(file.length()));

				//resp.setHeader("Content-Transfer-Encoding", "binary;");
				//resp.setHeader("Pragma", "no-cache;"); //http1.0
				//resp.setDateHeader("Expires", 0);
				//resp.setHeader ( "Cache-Control", "no-cache" ); //http1.1
				
				LogUtil.printLog(req.getRemoteAddr(), "downloaded file : " + file.getName());
			} catch (ClientAbortException e) {
				//do nothing
			} catch (Exception e) {
				LogUtil.printLog(req.getRemoteAddr(), "Server Logic error occured. ("+e.getLocalizedMessage()+")");
				e.printStackTrace();
			}
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



