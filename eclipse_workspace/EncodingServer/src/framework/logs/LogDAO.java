package framework.logs;

import java.sql.SQLException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import framework.jdbc.DBMng;

/**
 * LogStackService에서 사용되는 DAO
 * @author 박유현
 * @since 2019.09.30
 */
public class LogDAO {

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
			throw new SQLException(e);
		} finally {
			db.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getLogJSONList() throws SQLException {
		DBMng db = null;
		JSONArray res = new JSONArray();
		try {
			db = new DBMng();
			db.setQuery("select * from server_log_stack order by seq");
			db.execute();
			
			while (db.next()) {
				JSONObject temp = new JSONObject();
				temp.put("appName", db.getString("app_name"));
				temp.put("serverId", db.getString("storage_server_id"));
				temp.put("year", db.getString("year"));
				temp.put("month", db.getString("month"));
				temp.put("date", db.getString("date"));
				temp.put("hour", db.getString("hour"));
				temp.put("min", db.getString("min"));
				temp.put("duration", db.getInt("duration"));
				temp.put("regdate", db.getString("regdate"));
				res.add(temp);
			}
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			db.close();
		}
		return res;
	}
}
