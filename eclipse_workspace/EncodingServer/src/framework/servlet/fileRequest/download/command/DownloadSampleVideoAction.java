package framework.servlet.fileRequest.download.command;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.json.simple.JSONObject;

import framework.init.ServerConfig;
import framework.servlet.controller.handler.FileDownloadHandler;
import framework.servlet.fileRequest.SampleVideoDAO;
import framework.servlet.fileRequest.SampleVideoVO;
import framework.util.JSONUtil;
import framework.util.LogUtil;

/**
 * 업로드된 샘플 비디오의 원본파일을 다운로드하는 서블릿 핸들러 클래스
 * @author 박유현
 * @since 2019.10.13
 */
public class DownloadSampleVideoAction implements FileDownloadHandler {

	private static final int DOWNLOAD_BUFFER_SIZE = 1024 * 32;
	private SampleVideoDAO dao = new SampleVideoDAO();
	
	@Override
	public String getURL() {
		return "/download/original";
	}

	@SuppressWarnings("unchecked")
	@Override
	public File process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int fileId;
		try {
			fileId = Integer.parseInt(req.getParameter("id"));
		} catch (Exception e) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "id must be a integer value.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new NumberFormatException();
		}
		
		SampleVideoVO sampleVideo = dao.selectSampleVideoById(fileId);
		if (sampleVideo == null) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. Check id value.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("id not found");
		}
		if (sampleVideo.getStorageServerId() != ServerConfig.getServerId()) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. That file stored on another server.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("different server id");
		}
		
		File file = new File(sampleVideo.getDirectory());
		String fileName = sampleVideo.getFileName()+"."+sampleVideo.getFileExt();
		
		if (!file.exists()) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. It may have been deleted or moved.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("no such file detected.");
		}
		
		//마임타임 지정
		if (sampleVideo.getMimeType().equals("unknown"))
			resp.setContentType("application/octet-stream");
		else
			resp.setContentType(sampleVideo.getMimeType());
		
		//브라우저 별 Content-Disposition 지정
		String browserType = getBrowserType(req);
		if (browserType.contains("Chrome")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (browserType.contains("MSIE")) {
			String docName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
			resp.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
		} else if (browserType.contains("Firefox")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else if (browserType.contains("Opera")) {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		} else {
			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
		}
		
		FileInputStream fileInputStream = null;
		BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
		
		try {
			fileInputStream = new FileInputStream(file);
			
			byte[] outByte = new byte[DOWNLOAD_BUFFER_SIZE];
	
	        while(fileInputStream.read(outByte, 0, DOWNLOAD_BUFFER_SIZE) != -1) {
	        	outputStream.write(outByte, 0, DOWNLOAD_BUFFER_SIZE);
	        }
		} catch (ClientAbortException e){
			LogUtil.printLog(req.getRemoteAddr(), "request aborted");
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
		}
		
		return file;
	}
	
	private String getBrowserType(HttpServletRequest request) {
		String header =request.getHeader("User-Agent");
		if (header.contains("MSIE"))
			return "MSIE";
		else if(header.contains("Chrome"))
			return "Chrome";
		else if(header.contains("Opera"))
			return "Opera";

		return "Firefox";
	}
}
