package framework.servlet.fileRequest.upload.logic;

import java.util.Date;

/**
 * DB sample_video 테이블과 대응되는 VO
 * @author 박유현
 * @since 2019.10.13
 */
public class UploadSampleVideoRequestVO {

	private String fileName;
	private String fileExt;
	private String mimeType;
	private long volume;
	private String volumeStr;
	private String vCodec;
	private String aCodec;
	private int width;
	private int height;
	private int storageServerId;
	private String directory;
	private Date regdate;
	
	private String fileCategory;
	
	public UploadSampleVideoRequestVO(String fileName, String fileExt, String mimeType, long volume, String volumeStr,
			int storageServerId, String directory, Date regdate) {
		super();
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.mimeType = mimeType;
		this.volume = volume;
		this.volumeStr = volumeStr;
		this.vCodec = null;
		this.aCodec = null;
		this.width = -1;
		this.height = -1;
		this.storageServerId = storageServerId;
		this.directory = directory;
		this.regdate = regdate;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public String getVolumeStr() {
		return volumeStr;
	}
	public void setVolumeStr(String volumeStr) {
		this.volumeStr = volumeStr;
	}
	public String getvCodec() {
		return vCodec;
	}
	public void setvCodec(String vCodec) {
		this.vCodec = vCodec;
	}
	public String getaCodec() {
		return aCodec;
	}
	public void setaCodec(String aCodec) {
		this.aCodec = aCodec;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getStorageServerId() {
		return storageServerId;
	}
	public void setStorageServerId(int storageServerId) {
		this.storageServerId = storageServerId;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getFileCategory() {
		return fileCategory;
	}
	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}
	
}
