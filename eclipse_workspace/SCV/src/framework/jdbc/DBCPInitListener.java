package framework.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.dbcp.dbcp2.ConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.DriverManagerConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.PoolableConnection;
import org.apache.tomcat.dbcp.dbcp2.PoolableConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.PoolingDriver;
import org.apache.tomcat.dbcp.pool2.impl.GenericObjectPool;
import org.apache.tomcat.dbcp.pool2.impl.GenericObjectPoolConfig;

import framework.util.LogUtil;
import framework.util.PropertiesReader;

/**
 * DB Connection pool을 구현하는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebListener
public class DBCPInitListener implements ServletContextListener{
	private static String poolName;
	private GenericObjectPool<PoolableConnection> connectionPool;
	private PoolingDriver driver;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties tempProp = PropertiesReader.readPropString(sce.getServletContext().getInitParameter("globalConfig"));
		Properties prop = PropertiesReader.readPropertiesFile(sce.getServletContext().getRealPath(tempProp.getProperty("fileDestination")));
		loadDrivers(prop);
		initConnectionPool(prop);
		LogUtil.printLog("DBCP initiated successfully.");
	}
	
	/**
	 * DBCP 구성에 필요한 class를 로드시키는 메서드
	 * @param prop
	 */
	private void loadDrivers(Properties prop) {
		String JDBCDriverClass = prop.getProperty("JDBCDriverPath");
		try {
			Class.forName(JDBCDriverClass);
		} catch (ClassNotFoundException e) {
			LogUtil.printErrLog("fail to load JDBC Driver");
			throw new RuntimeException(e);
		}	
		
		String PoolingDriverClass = prop.getProperty("PoolingDriverPath");
		try {
			Class.forName(PoolingDriverClass);
		} catch (ClassNotFoundException e) {
			LogUtil.printErrLog("fail to load PoolingDriver");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 설정값으로 DBCP를 구현하는 메서드
	 * @param prop
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initConnectionPool(Properties prop) {
		try {
			String serverAddr = prop.getProperty("DBServerAddress");
			String serverPort = prop.getProperty("DBServerPort");
			String JDBCUrl = prop.getProperty("JDBCUrl");
			JDBCUrl = JDBCUrl.replace("{@address}", serverAddr);
			JDBCUrl = JDBCUrl.replace("{@port}", serverPort);
			String user = prop.getProperty("dbUser");
			String pw = prop.getProperty("dbPass");
			String validationQuery = prop.getProperty("validationQuery");
			String poolName = prop.getProperty("poolName");
			int minIdle = PropertiesReader.getIntProperty(prop, "minIdle", 10);
			int maxTotal = PropertiesReader.getIntProperty(prop, "maxTotal", 50);
			
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(JDBCUrl, user, pw);
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			if (validationQuery != null && validationQuery.isEmpty())
				poolableConnFactory.setValidationQuery(validationQuery);
			
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L);
			poolConfig.setTestWhileIdle(true);
			poolConfig.setMinIdle(minIdle);
			poolConfig.setMaxIdle(maxTotal);
			
			this.connectionPool = new GenericObjectPool<>(poolableConnFactory, poolConfig);
			poolableConnFactory.setPool(connectionPool);
			
			this.driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool(poolName, connectionPool);
			
			DBCPInitListener.poolName = poolName;

			poolableConnFactory.validateConnection(poolableConnFactory.getPool().borrowObject());
		} catch (Exception e) {
			throw new RuntimeException("fail to init Connection Pool", e);
		}
	}
	
	/**
	 * 서버가 꺼질 때 DBCP 자원을 반납하는 메서드
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			this.driver.closePool(DBCPInitListener.poolName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * poolName 확인을 위한 메서드
	 * @return
	 */
	public static String getPoolName() {
		return poolName;
	}
}
