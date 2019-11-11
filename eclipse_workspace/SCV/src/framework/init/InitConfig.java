package framework.init;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import framework.util.ByteUtil;
import framework.util.LogUtil;
import framework.util.PropertiesReader;

/**
 * 서버 설정값 초기화를 위한 클래스
 * @author 박유현
 * @since 2019.11.11
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
		ServerConfig.setIsDev(initProp.getProperty("devmode").equals("true"));
		setNetworkAddress();
		setPortNumber();
	}
	
	private static void setPortNumber() {
		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objectNames = null;
		try {
			objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String port = objectNames.iterator().next().getKeyProperty("port");
		ServerConfig.setServerPortNum(Integer.parseInt(port));
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

	}
	
	/**
	 * 서버 종료 시 DB 접근권한이 필요한 기능은 여기에 정의
	 * @see framework.jdbc.DBMngScheduler
	 */
	public static void destroyConfigWithDBAcessRequired() {

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
