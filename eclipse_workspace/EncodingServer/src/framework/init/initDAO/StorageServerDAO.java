package framework.init.initDAO;

import java.sql.SQLException;
import java.util.Date;

import framework.jdbc.DBMng;

/**
 * DB의 storage_server 테이블 대응 DAO
 * @author 박유현
 * @since 2019.10.12
 */
public class StorageServerDAO {

	public StorageServer findExistedServerByMacAddr(String macAddr) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from storage_server where mac_address = ?");
			db.setString(macAddr);
			db.execute();
			
			if (db.next())
				return new StorageServer(db.getInt(1), db.getString(2), db.getString(3), 
						db.getString(4), db.getInt(5), db.getDate(6));
			else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public void insertNewServer(String desc, String ipAddr, String macAddr, int status, Date regdate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into storage_server values (null, ?, ?, ?, ?, ?)");
			db.setString(desc);
			db.setString(ipAddr);
			db.setString(macAddr);
			db.setInt(status);
			db.setDate(regdate);
			db.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public void updateExistedServerByMacAddr(String desc, String ipAddr, String macAddr, int status, Date regdate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update storage_server set description = ?, address = ?, status = ?, regdate = ? where mac_address = ?");
			db.setString(desc);
			db.setString(ipAddr);
			db.setInt(status);
			db.setDate(regdate);
			db.setString(macAddr);
			db.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
}
