package storageServer.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import framework.jdbc.DBMng;
import framework.util.LogUtil;
import storageServer.vo.StorageServerVO;

public class StorageServerDAO {
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
