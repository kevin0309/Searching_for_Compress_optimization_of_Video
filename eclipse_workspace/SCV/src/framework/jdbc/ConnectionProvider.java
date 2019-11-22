package framework.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBCP에서 커넥션을 받아오는 클래스
 * @author 박유현
 * @since 2019.11.11
 */
public class ConnectionProvider {

	/**
	 * 현재 만들어진 DBCP에서 Connection을 받아오는 메서드
	 * @return
	 * @throws ConnectDBException
	 */
	public static Connection getConnection() {
		try {
			String poolAddr;

			//for Tomcat DBCP
			poolAddr = "jdbc:apache:commons:dbcp:";
			String poolNm = DBCPInitListener.getPoolName();
			poolAddr += poolNm;
			
			return DriverManager.getConnection(poolAddr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
