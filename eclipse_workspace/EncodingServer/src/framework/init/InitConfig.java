package framework.init;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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

		String containerName = sce.getServletContext().getServletContextName();
		if (containerName.equals("ROOT"))
			ServerConfig.setServiceContainerName("");
		else
			ServerConfig.setServiceContainerName("/"+containerName);
		ServerConfig.setProjectVersion(defProp.getProperty("projectVersion"));
		ServerConfig.setLogStackDirectory(initProp.getProperty("logStackDirectory"));
		ServerConfig.setLogStackInterval(PropertiesReader.getIntProperty(defProp, "logStackInterval", 0));
		ServerConfig.setIsDev(!initProp.getProperty("devmode").equals("true"));
		
		/*인코딩서버 전용설정********************************************/
		ServerConfig.setFFMPEGPath(initProp.getProperty("FFMPEGPath")+"/ffmpeg.exe");
		ServerConfig.setFFPROBEPath(initProp.getProperty("FFMPEGPath")+"/ffprobe.exe");
		String hddPathListSt = initProp.getProperty("fileStorageDirectoryPathList");
		String[] hddPathList = hddPathListSt.split("\\|\\|");
		ArrayList<ServerHDD> hddList = new ArrayList<>();
		for (String tempPath : hddPathList) {
			String name = tempPath.split("\\|")[0];
			String path = tempPath.split("\\|")[1];
			String tempDriveToken = path.substring(0,1);
			String tempDirPath = path.substring(2, path.length());
			ServerHDD tempHDD = new ServerHDD(name, tempDriveToken, tempDirPath);
			hddList.add(tempHDD);
		}
		ServerConfig.setHDDList(hddList);
		initFileStorage();
	}

	/**
	 * 파일 저장 디렉토리 생성
	 */
	private void initFileStorage() {
		for (ServerHDD hdd : ServerConfig.getHDDList()) {
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
}
