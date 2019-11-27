package framework.servlet.fileRequest.upload.logic;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import framework.SCVDAO;
import framework.init.ServerConfig;
import framework.init.ServerHddVO;
import framework.servlet.fileRequest.SampleVideoDAO;
import framework.servlet.fileRequest.SampleVideoVO;
import framework.servlet.fileRequest.upload.logic.PB.GetVideoMetadataOptions;
import framework.util.FileUtil;
import framework.util.GenerateFilePathFactory;
import framework.util.LogUtil;
import framework.util.windowsAppProcessing.WindowsAppProcessBuilder;
import framework.util.windowsAppProcessing.WindowsAppProcessOptions;
import works.encoding.EncodingQueue;
import works.encoding.EncodingQueueDAO;
import works.imageExtract.ExtractKeyframeCommand;

/**
 * 파일의 종류에따라 서로 다른 경로에 저장, 다른 기능을 수행하도록 분류하는 기능
 * @author 박유현
 * @since 2019.10.13
 */
public class ClassifyFileService {
	
	private File tempFile;
	private SampleVideoVO uploadReqVO;
	private GenerateFilePathFactory pathFactory;
	private String newPath;
	
	/**
	 * 파일을 분류하여 각각의 디렉토리에 저장
	 * 파일명 바꿔서 저장됨
	 * @param tempFile
	 * @param fileMIMEType
	 * @throws SQLException 
	 */
	public ClassifyFileService(File tempFile, ServerHddVO curHdd) throws SQLException {
		this.tempFile = tempFile;
		String fileName = FileUtil.getFileNameExceptExt(tempFile);
		
		//파일 마임타입 지정
		String mimeType = FileUtil.getMIMEType(tempFile);
		
		uploadReqVO = new SampleVideoVO(fileName, FileUtil.getExt(tempFile), mimeType, 
				tempFile.length(), FileUtil.generateVolumeStr(tempFile.length()), ServerConfig.getServerId(),
				null, new Date());
		
		//파일 카테고리 지정
		if (uploadReqVO.getMimeType().indexOf("video") > -1)
			uploadReqVO.setFileCategory("video");
		else if (uploadReqVO.getFileExt().toLowerCase().endsWith("smi") ||
				uploadReqVO.getFileExt().toLowerCase().endsWith("smi") ||
				uploadReqVO.getFileExt().toLowerCase().endsWith("smi"))
			uploadReqVO.setFileCategory("subtitle");
		else if (uploadReqVO.getMimeType().indexOf("image") > -1)
			uploadReqVO.setFileCategory("image");
		else if (uploadReqVO.getMimeType().indexOf("audio") > -1)
			uploadReqVO.setFileCategory("audio");
		else if (uploadReqVO.getMimeType().indexOf("text") > -1)
			uploadReqVO.setFileCategory("text");
		else if (uploadReqVO.getMimeType().indexOf("application") > -1)
			uploadReqVO.setFileCategory("application");
		else
			uploadReqVO.setFileCategory("ETC");

		pathFactory = new GenerateFilePathFactory(new Date(), curHdd);
		newPath = pathFactory.makeNewPath(null, uploadReqVO.getFileExt());
		uploadReqVO.setDirectory(newPath);
	}
	
	/**
	 * (파일디렉토리)/(현재연도)/(월)/
	 * 경로아래에 파일을 생성하도록 디렉토리를 미리 생성 후 각각의 파일 카테고리별 기능 실행
	 * @return UploadFileRequestVO
	 * @throws IOException
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public SampleVideoVO process() throws IOException, SQLException, ParseException {
		//temp에 저장되어있는 파일을 옮김
		FileUtil.moveFile(tempFile, newPath);
				
		//각 카테고리별 특수 파일 프로세싱
		if (uploadReqVO.getFileCategory().equals("video"))
			processVideo();
		else if (uploadReqVO.getFileCategory().equals("subtitle"))
			processSubtitle();
		else if (uploadReqVO.getFileCategory().equals("image"))
			processImage();
		else if (uploadReqVO.getFileCategory().equals("audio"))
			processAudio();

		return uploadReqVO;
	}
	
	/**
	 * 
	 * @param tempFile
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws AddToEncodingQueueException 
	 */
	public void processVideo() throws SQLException, ParseException {
		//비디오 해상도, 코덱 확인
		WindowsAppProcessOptions getVideoMetadataOptions = 
			new GetVideoMetadataOptions(ServerConfig.getFFPROBEPath(), newPath);
		WindowsAppProcessBuilder wapb = new WindowsAppProcessBuilder();
		try {
			wapb.process(getVideoMetadataOptions.generateCmdLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String wapbRes = "";
		for (String out : wapb.getLogs())
			wapbRes += out;
		JSONParser parser = new JSONParser();
		JSONObject metaData = (JSONObject) parser.parse(wapbRes);
		JSONArray streams = (JSONArray) metaData.get("streams");
		JSONObject videoMetaData = null, audioMetaData = null;
		for (Object stream : streams) {
			JSONObject tempStream = (JSONObject) stream;
			if (tempStream.get("codec_type").equals("video") && videoMetaData == null)
				videoMetaData = tempStream;
			if (tempStream.get("codec_type").equals("audio") && audioMetaData == null)
				audioMetaData = tempStream;
		}
		long videoWidth = (Long)videoMetaData.get("width");
		long videoHeight = (Long)videoMetaData.get("height");
		String videoCodec = (String)videoMetaData.get("codec_name");
		String audioCodec = (String)audioMetaData.get("codec_name");
		if (videoCodec == null)
			videoCodec = "unknown";
		if (audioCodec == null)
			audioCodec = "unknown";
		
		uploadReqVO.setWidth((int)videoWidth);
		uploadReqVO.setHeight((int)videoHeight);
		uploadReqVO.setvCodec(videoCodec);
		uploadReqVO.setaCodec(audioCodec);

		new SampleVideoDAO().insertNewSampleVideo(uploadReqVO);
		int fid = new SCVDAO().lastInsertId();
		new EncodingQueueDAO().insertQueue(fid, ServerConfig.getServerId(), EncodingQueue.ORIGINAL_CONV_PRESET_CODE);
		//extract key frames
		/*String newThumbsPath = pathFactory.makeNewThumbPath(null);
		ExtractKeyframeCommand cmd2 = new ExtractKeyframeCommand(newPath, newThumbsPath + "thumb%03d.jpg");
		WindowsAppProcessBuilder wapb2 = new WindowsAppProcessBuilder(newThumbsPath+"log.txt");
		LogUtil.printLog("Extract Thumbnail start, sample video target : " + newPath);
		try {
			if (wapb2.process(cmd2.generateCmdLine()))
				LogUtil.printLog("Extract Thumbnail complete, sample video target : " + newThumbsPath);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
	/**
	 * 
	 * @throws SQLException 
	 * @throws AddToEncodingQueueException 
	 */
	private void processAudio() throws SQLException {
		
	}
	
	/**
	 * 썸네일용 리사이즈한 파일 따로 같은 경로 내 thumbnail 폴더에 저장 (320x320 크기, jpg)
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void processImage() {
		//쓰레드 만들어서 진행
		(new Thread(new Runnable() {
			@Override
			public void run() {
				File imgFile = new File(newPath);
				//썸네일 이미지 생성 320x320 크기, jpg
				try {
					Image originalImage = ImageIO.read(imgFile);
					Image thumbNail = originalImage.getScaledInstance(320, 320, Image.SCALE_SMOOTH);
					BufferedImage newImage = new BufferedImage(320, 320, BufferedImage.TYPE_INT_RGB);
					Graphics g = newImage.getGraphics();
					g.drawImage(thumbNail, 0, 0, null);
					g.dispose();
					
					File newThumbnailImageFile = new File(pathFactory.makeNewPath("thumbnail(320)", "jpg"));
					ImageIO.write(newImage, "jpg", newThumbnailImageFile);
				} catch (NullPointerException | IOException e) {
					LogUtil.printErrLog("can't convert to Thumbnail image.");
				}
			}
		})).start();
	}
	
	/**
	 * smi, srt, ass 자막파일
	 * SMI, SRT to ASS file
	 * @throws SQLException 
	 * @throws AddToEncodingQueueException 
	 */
	private void processSubtitle() throws SQLException {
		/*//인코딩용 자막 생성시 사용할 인풋 자막파일 캐릭터셋 확인
		String charsetDet = null;
		File subFile = new File(pathFactory.toAbsolutePathString(null, uploadReqVO.getOriginalFileExt()));
		try {
			charsetDet = FileUtil.detectCharset(subFile);
		} catch (IOException e) {
			LogUtil.printErrLog(uploadReqVO.getRequestIP(), "detect sub file charset error");
			subFile.delete();
			throw new AddToEncodingQueueException();
		}
		
		String charset = "";
		//실험으로 얻어낸 보정값들
		if (charsetDet == null)
			charset = "CP949";
		else if (charsetDet.equals("EUC-KR"))
			charset = "EUCKR";
		else if (charsetDet.equals("UTF-8") || charsetDet.equals("UTF-16LE"))
			charset = "UTF-8";
		else
			charset = charsetDet;
		
		//ASS파일 인코딩시 파일경로에 한글있으면 안됨 -> 랜덤문자열4자 + 현재시간
		String newSubFileName = pathFactory.toNewNoKoreanFileNameString("ass");
		String newSubFilePath = pathFactory.toDirAbsolutePathString("ass")+"/"+newSubFileName;
		WindowsAppProcessOptions encodingOptionsAss = new SubtitleEncodingOptions(
				ServerConfig.getFFMPEGPath(), 
				pathFactory.toAbsolutePathString(null, uploadReqVO.getOriginalFileExt()),
				newSubFilePath, 
				true, 
				charset
			);
		EncodingQueueElement workSub = new EncodingQueueElement(
				pathFactory.toAbsolutePathString(null, uploadReqVO.getOriginalFileExt()),
				newSubFilePath,
				uploadReqVO.getVolume(),
				encodingOptionsAss.generateCmdLine(),
				uploadReqVO.getFileID(),
				new UploadFileServerDirectoryVO(
						"subtitle_ass",
						pathFactory.toDirRelativePathString("ass")+"/"+newSubFileName,
						"ass"
					)
			);
		
		//자막은 인코딩 대기열 무시하고 바로 프로세스 만들어 실행
		if (!(new WindowsAppProcessBuilder(false).process(workSub.getEncodingOption())))
			LogUtil.printErrLog("target : "+workSub.getOriginalFilePath());
		else {
			workSub.getUploadReqVO().setVolume(new File(workSub.getTargetFilePath()).length());
			uploadReqVO.getDirList().add(workSub.getUploadReqVO());
			LogUtil.printLog("encoding complete. ["+workSub.getTargetFilePath()+"]");
		}*/
	}
}
