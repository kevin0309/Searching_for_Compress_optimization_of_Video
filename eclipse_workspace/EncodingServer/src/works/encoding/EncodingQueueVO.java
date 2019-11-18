package works.encoding;

import java.util.Date;

import framework.servlet.fileRequest.SampleVideoVO;

/**
 * DB encoding_queue 테이블 대응 VO
 * @author 박유현
 * @since 2019.10.13
 */
public class EncodingQueueVO {

	private int seq;
	private int fileId;
	private String presetCode;
	private String status;
	private long eVolume;
	private Date sDate;
	private Date eDate;
	private int assignedServerId;
	private String newDirectory;
	private Date regdate;
	
	private SampleVideoVO video;
	
	public EncodingQueueVO(int seq, int fileId, String presetCode, String status, long eVolume,
			Date sDate, Date eDate, int assignedServerId, String newDirectory, Date regdate) {
		super();
		this.seq = seq;
		this.fileId = fileId;
		this.presetCode = presetCode;
		this.status = status;
		this.eVolume = eVolume;
		this.sDate = sDate;
		this.eDate = eDate;
		this.assignedServerId = assignedServerId;
		this.newDirectory = newDirectory;
		this.regdate = regdate;
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getPresetCode() {
		return presetCode;
	}
	public void setPresetCode(String presetCode) {
		this.presetCode = presetCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long geteVolume() {
		return eVolume;
	}
	public void seteVolume(long eVolume) {
		this.eVolume = eVolume;
	}
	public Date getsDate() {
		return sDate;
	}
	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}
	public Date geteDate() {
		return eDate;
	}
	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}
	public int getAssignedServerId() {
		return assignedServerId;
	}
	public void setAssignedServerId(int assignedServerId) {
		this.assignedServerId = assignedServerId;
	}
	public String getNewDirectory() {
		return newDirectory;
	}
	public void setNewDirectory(String newDirectory) {
		this.newDirectory = newDirectory;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public SampleVideoVO getVideo() {
		return video;
	}
	public void setVideo(SampleVideoVO video) {
		this.video = video;
	}
}
