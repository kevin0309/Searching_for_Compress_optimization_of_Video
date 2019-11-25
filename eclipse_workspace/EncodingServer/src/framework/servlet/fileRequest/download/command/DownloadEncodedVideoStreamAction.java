package framework.servlet.fileRequest.download.command;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.json.simple.JSONObject;

import framework.init.ServerConfig;
import framework.servlet.controller.handler.FileDownloadHandler;
import framework.util.FileUtil;
import framework.util.JSONUtil;
import framework.util.LogUtil;
import works.encoding.EncodingQueueDAO;
import works.encoding.EncodingQueueVO;

/**
 * 인코딩 결과 비디오파일을 스트리밍 형식으로 다운로드하는 서블릿 핸들러 클래스
 * @author 박유현
 * @since 2019.11.26
 */
public class DownloadEncodedVideoStreamAction implements FileDownloadHandler {

	private static final int DOWNLOAD_BUFFER_SIZE = 1024 * 32;
	private EncodingQueueDAO dao = new EncodingQueueDAO();
	
	@Override
	public String getURL() {
		return "/download/stream";
	}

	@SuppressWarnings("unchecked")
	@Override
	public File process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int encodingQueueId;
		try {
			encodingQueueId = Integer.parseInt(req.getParameter("id"));
		} catch (Exception e) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "id must be a integer value.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new NumberFormatException();
		}
		
		EncodingQueueVO encodedVideo = dao.getEncodingWorkBySeq(encodingQueueId);
		if (encodedVideo == null) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. Check id value.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("id not found");
		}
		if (encodedVideo.getAssignedServerId() != ServerConfig.getServerId()) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. That file stored on another server.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("different server id");
		}
		
		File file = new File(encodedVideo.getNewDirectory());
		String[] temp = encodedVideo.getNewDirectory().split("/");
		String fileName = temp[temp.length-1];
		
		if (!file.exists()) {
			JSONObject resMsg = new JSONObject();
			resMsg.put("error detail", "The file does not exist. It may have been deleted or moved.");
			JSONUtil.setJSONResponse(resMsg, req, resp);
			throw new FileNotFoundException("no such file detected.");
		}

		//용량 지정
		resp.addHeader("Content-Length", Long.toString(file.length()));
		
		resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
		resp.setCharacterEncoding(ServerConfig.getServerEncodingCharSet());
		
		String range = req.getHeader("Range");
		if (range == null)
			range = "bytes=0-";
		int i = range.indexOf("=");
		int j = range.indexOf("-");
		long startPoint = Long.parseLong(range.substring(i + 1, j));
		
		resp.setHeader("Accept-Ranges", "bytes");
		resp.setHeader("Content-Range", "bytes " + startPoint + "-"+(file.length()-1)+"/" + file.length());
		resp.setHeader("Range", "bytes " + startPoint + "-"+(file.length()-1)+"/" + file.length());
		
		//마임타임 지정
		String mimeType = FileUtil.getMIMEType(file);
		if (mimeType.equals("unknown"))
			resp.setContentType("application/octet-stream");
		else
			resp.setContentType(mimeType);
		
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
		
		
		RandomAccessFile rf = new RandomAccessFile(file, "r");
		rf.seek(startPoint);
		byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
		int num = 0;
		BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
		try {
			while (startPoint < file.length() - 1 && (num = rf.read(buffer)) != -1) {
			  outputStream.write(buffer, 0, num);
			  outputStream.flush();
			  startPoint += DOWNLOAD_BUFFER_SIZE;
			}
		} catch(ClientAbortException e) {
			LogUtil.printLog(req.getRemoteAddr(), "request aborted");
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			rf.close();
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
