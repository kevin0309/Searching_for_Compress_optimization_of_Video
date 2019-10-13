package framework.init;

import java.io.File;

/**
 * 인코딩에서 사용될 하드디스크에 대한 정보 
 * @author 박유현
 * @since 2019.09.30
 */
public class ServerHddVO {

	private String name;
	private String token;
	private String path;
	private long driveTotalSpace;
	private long driveUsableSpace;
	private boolean currentUse;
	
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @param name 하드디스크 별칭
	 * @param token 하드디스크명 (ex: "C" or "D")
	 * @param path 하드디스크 내 저장될 기본 디렉토리
	 */
	public ServerHddVO(String name, String token, String path) {
		super();
		this.name = name;
		this.token = token;
		this.path = path;
		this.driveTotalSpace = new File(token+":/").getTotalSpace();
		driveUsableSpace = new File(token+":/").getUsableSpace();
		this.currentUse = false;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @return 하드디스크 별칭
	 */
	public String getName() {
		return name;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @return 하드디스크명 (ex: "C" or "D")
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @return 하드디스크 내 저장될 기본 디렉토리
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @return 하드디스크 전체용량
	 */
	public long getDriveTotalSpace() {
		return driveTotalSpace;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @return 하드디스크 사용가능한 용량
	 */
	public long getDriveUsableSpace() {
		return driveUsableSpace;
	}
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @param driveUsableSpace 하드디스크 사용가능한 용량
	 */
	public void setDriveUsableSpace(long driveUsableSpace) {
		this.driveUsableSpace = driveUsableSpace;
	}
	/**
	 * 현재 사용중인 하드디스크인지 (현재 서버 어플리케이션 기준)
	 * @return true if 사용중
	 */
	public boolean isCurrentUse() {
		return currentUse;
	}
	/**
	 * 현재 사용중인 하드디스크인지 (현재 서버 어플리케이션 기준)
	 * @param currentUse true if 사용중
	 */
	public void setCurrentUse(boolean currentUse) {
		this.currentUse = currentUse;
	}
	
	/**
	 * 디렉토리 절대경로 리턴
	 * @return 디렉토리 절대경로
	 */
	public String getFullPath() {
		return token+":"+path;
	}
}
