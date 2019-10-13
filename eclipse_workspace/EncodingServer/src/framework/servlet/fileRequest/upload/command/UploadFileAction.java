package framework.servlet.fileRequest.upload.command;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import framework.init.ServerConfig;
import framework.init.ServerHDD;
import framework.servlet.controller.handler.FileUploadHandler;
import framework.servlet.fileRequest.HddSelector;
import framework.servlet.fileRequest.upload.logic.ClassifyFileService;
import framework.servlet.fileRequest.upload.logic.UploadSampleVideoRequestVO;

/**
 * 요청받은 파일을 서버 파일 디렉토리에 저장하는 기능
 * @author 박유현
 * @since 2019.10.12
 */
public class UploadFileAction implements FileUploadHandler {

	private final int MAX_FILE_SIZE = 1024 * 1024 * 1024 * 2 - 1;
	
	@Override
	public String getURL() {
		return "/upload/file";
	}

	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HddSelector hs = HddSelector.getInstance();
		ServerHDD hdd = hs.getHDD(1);
		try {
			//temp 폴더에 일단 저장
			MultipartRequest mReq = new MultipartRequest(req, hdd.getFullPath()+"/temp", MAX_FILE_SIZE, ServerConfig.getServerEncodingCharSet());
			File file = mReq.getFile("upload_file");
			ClassifyFileService cfs = new ClassifyFileService(file, hdd);
			UploadSampleVideoRequestVO res = cfs.process();
			return res.getMimeType()+" - "+res.getFileName()+"."+res.getFileExt();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			hs.close();
		}
	}
	
}
