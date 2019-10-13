package framework.init;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import framework.init.initDAO.StorageServerVO;
import framework.init.initDAO.StorageServerDAO;
import framework.util.ByteUtil;
import framework.util.FileUtil;
import framework.util.LogUtil;
import framework.util.PropertiesReader;

/**
 * 서버 설정값 초기화를 위한 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebListener
public class InitConfig implements ServletContextListener {
	/**
	 * 서버 전역설정값 초기화 
	 * @see /WebContent/WEB-INF/web.xml
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties tempInitProp = PropertiesReader.readPropString(sce.getServletContext().getInitParameter("initConfig"));
		Properties initProp = PropertiesReader.readPropertiesFile(sce.getServletContext().getRealPath(tempInitProp.getProperty("fileDestination")));
		Properties tempDefProp = PropertiesReader.readPropString(sce.getServletContext().getInitParameter("globalConfig"));
		Properties defProp = PropertiesReader.readPropertiesFile(sce.getServletContext().getRealPath(tempDefProp.getProperty("fileDestination")));
		
		ServerConfig.setDateFormat(defProp.getProperty("dateFormat"));

		//서버 닉네임을 설정파일이름으로 설정
		String configFileDest = tempInitProp.getProperty("fileDestination");
		String[] temp = configFileDest.split("/");
		configFileDest = temp[temp.length-1];
		ServerConfig.setServerNickname(configFileDest.split("\\.")[0]);
		
		String containerName = sce.getServletContext().getServletContextName();
		if (containerName.equals("ROOT")) {
			ServerConfig.setServiceContainerName("");
			ServerConfig.setServerPortNum(80);
		}
		else {
			ServerConfig.setServiceContainerName("/"+containerName);
			ServerConfig.setServerPortNum(8080);
		}
		ServerConfig.setProjectVersion(defProp.getProperty("projectVersion"));
		ServerConfig.setLogStackDirectory(initProp.getProperty("logStackDirectory"));
		ServerConfig.setLogStackInterval(PropertiesReader.getIntProperty(defProp, "logStackInterval", 0));
		ServerConfig.setIsDev(initProp.getProperty("devmode").equals("true"));
		setNetworkAddress();
		
		//사용가능한 디스크 스토리지 점검
		String hddPathListSt = initProp.getProperty("fileStorageDirectoryPathList");
		String[] hddPathList = hddPathListSt.split("\\|\\|");
		ArrayList<ServerHddVO> hddList = new ArrayList<>();
		for (String tempPath : hddPathList) {
			String name = tempPath.split("\\|")[0];
			String path = tempPath.split("\\|")[1];
			String tempDriveToken = path.substring(0,1);
			String tempDirPath = path.substring(2, path.length());
			ServerHddVO tempHDD = new ServerHddVO(name, tempDriveToken, tempDirPath);
			hddList.add(tempHDD);
		}
		ServerConfig.setHDDList(hddList);
		initFileStorage();
		
		/*인코딩서버 전용설정********************************************/
		ServerConfig.setFFMPEGPath(initProp.getProperty("FFMPEGPath")+"/ffmpeg.exe");
		ServerConfig.setFFPROBEPath(initProp.getProperty("FFMPEGPath")+"/ffprobe.exe");
	}
	
	/**
	 * 서버PC MAC 주소를 가져옴
	 * @return MAC 주소 hex String
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private static void setNetworkAddress() {
		InetAddress ip;
		try {
			//ip 확인
			ip = InetAddress.getLocalHost();
			ServerConfig.setIpAddr(ip.getHostAddress());
			//ip로부터 mac주소 확인
			NetworkInterface netIf = NetworkInterface.getByInetAddress(ip);
			ServerConfig.setMacAddr(ByteUtil.byteArrayToHexString(netIf.getHardwareAddress()));
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			LogUtil.printErrLog("Can't find server MAC address. plz check your network interface.");
		}
	}
	
	/**
	 * 서버 초기 설정 중 DB 접근권한이 필요한 기능은 여기에 정의
	 * @see framework.jdbc.DBMngScheduler
	 */
	public static void initConfigWithDBAcessRequired() {
		setServerStatus();
		ServerConfig.setServerId(getServerId());
	}
	
	/**
	 * DB의 storage_server 테이블에 등록되지 않은 서버라면 등록하고, 온라인상태로 변경
	 */
	private static void setServerStatus() {
		StorageServerDAO dao = new StorageServerDAO();
		StorageServerVO server = dao.findExistedServerByMacAddr(ServerConfig.getMacAddr());
		//등록된 mac 주소가 없을경우
		if (server == null) {
			LogUtil.printLog("The server profile not found at DB. Make new profile with this server.");
			dao.insertNewServer(ServerConfig.getServerNickname(), ServerConfig.getIpAddr()+":"+ServerConfig.getServerPortNum(),
					ServerConfig.getMacAddr(), 1, new Date());
		}
		else {
			if (server.getStatus() == 1)
				LogUtil.printErrLog("Unknown error! server status is already online.");
			else if (server.getStatus() == -1)
				LogUtil.printErrLog("Unknown error! server status is error occured.");
			
			LogUtil.printLog("Update server profile status to online.");
			dao.updateExistedServerByMacAddr(ServerConfig.getServerNickname(), ServerConfig.getIpAddr()+":"+ServerConfig.getServerPortNum(), 
					ServerConfig.getMacAddr(), 1, new Date());
		}
	}
	
	/**
	 * DB의 storage_server 테이블에 등록된 서버의 seq값을 반환
	 * @return storage_server.seq
	 */
	private static int getServerId() {
		StorageServerDAO dao = new StorageServerDAO();
		StorageServerVO server = dao.findExistedServerByMacAddr(ServerConfig.getMacAddr());
		return server.getSeq();
	}
	
	/**
	 * 서버 종료 시 DB 접근권한이 필요한 기능은 여기에 정의
	 * @see framework.jdbc.DBMngScheduler
	 */
	public static void destroyConfigWithDBAcessRequired() {
		setServerStatusToOffline();
	}
	
	/**
	 * DB의 storage_server 테이블에 등록된 서버의 status를 0으로 수정(오프라인상태)
	 */
	private static void setServerStatusToOffline() {
		StorageServerDAO dao = new StorageServerDAO();
		LogUtil.printLog("Update server profile status to offline.");
		dao.updateExistedServerByMacAddr(ServerConfig.getServerNickname(), ServerConfig.getIpAddr()+":"+ServerConfig.getServerPortNum(), 
				ServerConfig.getMacAddr(), 0, new Date());
	}
	
	/**
	 * 필터에서 설정된 값으로 전역설정값 적용시키기 위한 메서드
	 * @param charSet
	 * @see framework.servlet.CharsetEncodingFilter
	 */
	public static void setServerEncodingCharSet(String charSet) {
		ServerConfig.setServerEncodingCharSet(charSet);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	/**
	 * 파일 저장 디렉토리 생성
	 */
	private void initFileStorage() {
		for (ServerHddVO hdd : ServerConfig.getHDDList()) {
			File directory = new File(hdd.getToken()+":"+hdd.getPath()+"/temp");
			if (!directory.exists())
				directory.mkdirs();
			try {
				LogUtil.printLog("1. Check for file-storage directory - " + hdd.getToken()+":"+hdd.getPath());
				File fileDet = new File(hdd.getToken()+":"+hdd.getPath()+"/temp/test.txt");
				FileUtil.write(fileDet.getAbsolutePath(), "Hi there! This is test file!");
				LogUtil.printLog("2. HDD : ["+hdd.getToken()+" drive] (UsableSpace : "+FileUtil.generateVolumeStr(hdd.getDriveUsableSpace())+" / TotalSpace : "+FileUtil.generateVolumeStr(hdd.getDriveTotalSpace())+")");
				if (fileDet.exists())
					LogUtil.printLog("3. Check for file-storage directory - OK.");
				else {
					LogUtil.printLog("3. Check for file-storage directory - make new directory...");
					fileDet.getParentFile().mkdirs();
					fileDet.createNewFile();
				}
			} catch (Exception e) {
				LogUtil.printErrLog("3. Check for file-storage directory - ERROR! check your configuration.");
				return;
			}
		}
		LogUtil.printLog("The hard disks are ready for use.");
	}
}
