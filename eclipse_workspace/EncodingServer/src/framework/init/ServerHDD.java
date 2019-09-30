package framework.init;

import java.io.File;

/**
 * 인코딩에서 사용될 하드디스크에 대한 정보 
 * @author 박유현
 * @since 2019.09.30
 */
public class ServerHDD {

	private String name;
	private String token;
	private String path;
	private long driveTotalSpace;
	
	/**
	 * 인코딩에서 사용될 하드디스크에 대한 정보 
	 * @param name 하드디스크 별칭
	 * @param token 하드디스크명 (ex: "C" or "D")
	 * @param path 하드디스크 내 저장될 기본 디렉토리
	 */
	public ServerHDD(String name, String token, String path) {
		super();
		this.name = name;
		this.token = token;
		this.path = path;
		this.driveTotalSpace = new File(token+":/").getTotalSpace();
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
		return new File(token+":/").getUsableSpace();
	}
}
