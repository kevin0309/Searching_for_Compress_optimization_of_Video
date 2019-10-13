package framework.logs;

import java.sql.SQLException;
import java.util.Date;

import framework.jdbc.DBMng;
import framework.util.LogUtil;

/**
 * DB server_log_stack 테이블에 대응되는 DAO
 * @author 박유현
 * @since 2019.09.30
 */
public class LogDAO {

	/**
	 * 새로운 로그에 대한 레코드 생성
	 * @param appName
	 * @param serverID
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param min
	 * @param duration
	 * @param regDate
	 * @throws SQLException
	 */
	public void insertNewLogFile(String appName, int serverID, String year, String month, String date, String hour, String min, int duration, Date regDate) throws SQLException {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into server_log_stack values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			db.setString(appName);
			db.setInt(serverID);
			db.setString(year);
			db.setString(month);
			db.setString(date);
			db.setString(hour);
			db.setString(min);
			db.setInt(duration);
			db.setDate(regDate);
			db.execute();
		} catch (Exception e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new SQLException(e);
		} finally {
			db.close();
		}
	}
}
