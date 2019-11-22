package framework.init;

import framework.util.LogUtil;

/**
 * 서버에서 사용되는 전역 설정값만 저장해두는 클래스. VO (Value Object)
 * @author 박유현
 * @since 2019.11.11
 */
public class ServerConfig {
	
	private static String serviceContainerName;
	private static String projectVersion;
	private static String dateFormat;
	private static String serverEncodingCharSet;
	private static String logStackDirectory;
	private static int logStackInterval;
	private static boolean isDev;
	private static String ipAddr;
	private static int serverPortNum;
	private static String macAddr;

	/**
	 * 서버 전역설정값
	 * @return 서비스 컨테이너 이름 ROOT의 경우 ""로 대체됨
	 */
	public static String getServiceContainerName() {
		return serviceContainerName;
	}
	static void setServiceContainerName(String serviceContainerName) {
		ServerConfig.serviceContainerName = serviceContainerName;
		LogUtil.printLog("The service container name has been set to [" + serviceContainerName + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 프로젝트 버전
	 */
	public static String getProjectVersion() {
		return projectVersion;
	}
	static void setProjectVersion(String projectVersion) {
		ServerConfig.projectVersion = projectVersion;
		LogUtil.printLog("The project version has been set to [" + projectVersion + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 날짜 형식
	 */
	public static String getDateFormat() {
		return dateFormat;
	}
	static void setDateFormat(String dateFormat) {
		ServerConfig.dateFormat = dateFormat;
		LogUtil.printLog("The server date format has been set to [" + dateFormat + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 인코딩 캐릭터셋
	 */
	public static String getServerEncodingCharSet() {
		return serverEncodingCharSet;
	}
	static void setServerEncodingCharSet(String serverEncodingCharSet) {
		ServerConfig.serverEncodingCharSet = serverEncodingCharSet;
		LogUtil.printLog("The server encoding charset has been set to [" + serverEncodingCharSet + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 로그파일이 저장될 디렉토리
	 * @see framework.logs.LogStackService
	 */
	public static String getLogStackDirectory() {
		return logStackDirectory;
	}
	static void setLogStackDirectory(String logStackDirectory) {
		ServerConfig.logStackDirectory = logStackDirectory;
		LogUtil.printLog("The server log stack directory has been set to [" + logStackDirectory + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 로그파일이 생성되는 주기 (분단위)
	 * @see framework.logs.LogStackService
	 */
	public static int getLogStackInterval() {
		return logStackInterval;
	}
	static void setLogStackInterval(int logStackInterval) {
		ServerConfig.logStackInterval = logStackInterval;
		LogUtil.printLog("The server log stack interval has been set to [" + logStackInterval + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 개발자모드 - true / 일반사용자모드 - false
	 */
	public static boolean isDev() {
		return isDev;
	}
	static void setIsDev(boolean isDev) {
		ServerConfig.isDev = isDev;
		if (isDev) {
			System.out.println("***|  ***    *****  *    *       *     *   ****   ***    *****   |***");
			System.out.println("***|  *   *  *      *    *       **   **  *    *  *   *  *       |***");
			System.out.println("***|  *   *  *****  *    *       * * * *  *    *  *   *  *****   |***");
			System.out.println("***|  *   *  *       *  *        * * * *  *    *  *   *  *       |***");
			System.out.println("***|  ***    *****    **         *  *  *   ****   ***    *****   |***");
			LogUtil.printLog("The server now runs in developer mode.");
		}
	}
	/**
	 * 서버 전역설정값
	 * @return 현재 서버의 IP 주소
	 */
	public static String getIpAddr() {
		return ipAddr;
	}
	static void setIpAddr(String ipAddr) {
		ServerConfig.ipAddr = ipAddr;
		LogUtil.printLog("The server IP address is [" + ipAddr + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 현재 서버의 port number
	 */
	public static int getServerPortNum() {
		return serverPortNum;
	}
	static void setServerPortNum(int serverPortNum) {
		ServerConfig.serverPortNum = serverPortNum;
		LogUtil.printLog("The server port number is [" + serverPortNum + "].");
	}
	/**
	 * 서버 전역설정값
	 * @return 현재 서버의 MAC 주소
	 */
	public static String getMacAddr() {
		return macAddr;
	}
	static void setMacAddr(String macAddr) {
		ServerConfig.macAddr = macAddr;
		LogUtil.printLog("The server MAC address is [" + macAddr + "].");
	}
	
}
