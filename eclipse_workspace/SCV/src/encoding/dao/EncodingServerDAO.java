package encoding.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import encoding.vo.EncodingServerVO;
import framework.jdbc.DBMng;
import framework.util.LogUtil;

public class EncodingServerDAO {
	public ArrayList<EncodingServerVO> getServerList(int offset, int amount) {
		DBMng db = null;
		ArrayList<EncodingServerVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from storage_server limit ?, ?");
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next())
				res.add(new EncodingServerVO(db.getInt(1), db.getString(2), db.getString(3), 
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
