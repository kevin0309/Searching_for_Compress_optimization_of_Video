package framework.init.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import framework.jdbc.DBMng;
import framework.util.LogUtil;

/**
 * DB의 storage_server 테이블 대응 DAO
 * @author 박유현
 * @since 2019.10.12
 */
public class StorageServerDAO {

	/**
	 * mac address로 storage_server 테이블 조회
	 * @param macAddr
	 * @return
	 */
	public StorageServerVO findExistedServerByMacAddr(String macAddr) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from storage_server where mac_address = ?");
			db.setString(macAddr);
			db.execute();
			
			if (db.next())
				return new StorageServerVO(db.getInt(1), db.getString(2), db.getString(3), 
						db.getString(4), db.getInt(5), db.getDate(6));
			else
				return null;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * 새로운 서버 프로필 작성
	 * @param desc
	 * @param ipAddr
	 * @param macAddr
	 * @param status
	 * @param regdate
	 */
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
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * 기존 서버 프로필 정보 수정
	 * @param desc
	 * @param ipAddr
	 * @param macAddr
	 * @param status
	 * @param regdate
	 */
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
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public ArrayList<StorageServerVO> getServerList(int offset, int amount) {
		DBMng db = null;
		ArrayList<StorageServerVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from storage_server limit ?, ?");
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next())
				res.add(new StorageServerVO(db.getInt(1), db.getString(2), db.getString(3), 
						db.getString(4), db.getInt(5), db.getDate(6)));
			return res;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public int getTotalRowCnt() {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select count(1) cnt from storage_server");
			db.execute();
			
			if (db.next())
				return db.getInt("cnt");
			else
				return -1;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
}
