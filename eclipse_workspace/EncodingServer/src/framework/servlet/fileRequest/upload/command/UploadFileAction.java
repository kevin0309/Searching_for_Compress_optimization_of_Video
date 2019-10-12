package framework.servlet.fileRequest.upload.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.servlet.controller.handler.FileUploadHandler;

/**
 * 요청받은 파일을 서버 파일 디렉토리에 저장하는 기능
 * @author 유현
 * @since 2019.10.12
 */
public class UploadFileAction implements FileUploadHandler {

	//private final int MAX_FILE_SIZE = 1024 * 1024 * 1024 * 2 - 1;
	
	@Override
	public String getURL() {
		return "/upload/file";
	}

	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		/*try {
			//temp 폴더에 일단 저장
			MultipartRequest mReq = new MultipartRequest(req, ServerConfig.getFileStorageDirectory().getFullPathForServer()+"/temp", MAX_FILE_SIZE, ServerConfig.getServerEncodingCharSet());
			
			File file = mReq.getFile("upload_file"); //upload한 파일의 파라미터 키값은 upload_file이어야 함
			String requestUserID = (String) req.getSession().getAttribute("userID");
			
			ClassifyFileService cfs = new ClassifyFileService(file, null, requestUserID, req.getRemoteAddr());
			UploadFileRequestVO res = cfs.process();
			return res.getFileMIMEType()+" - "+res.getOriginalFileName()+"."+res.getOriginalFileExt();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
		return null;
	}
	
}
