package framework.init;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
