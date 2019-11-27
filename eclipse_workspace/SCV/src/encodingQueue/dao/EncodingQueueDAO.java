package encodingQueue.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import encodingQueue.vo.EncodingQueueVO;
import framework.jdbc.DBMng;
import framework.util.LogUtil;

public class EncodingQueueDAO {

	public ArrayList<EncodingQueueVO> getEncodingQueueList(int offset, int amount) {
		DBMng db = null;
		ArrayList<EncodingQueueVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue order by seq desc limit ?, ?");
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next())
				res.add(new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), db.getString(4), db.getLong(5),
						db.getDate(6), db.getDate(7), db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11)));
			return res;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public EncodingQueueVO getEncodingQueue(int seq) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue where seq = ?");
			db.setInt(seq);
			db.execute();
			
			if (db.next())
				return new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), db.getString(4), db.getLong(5),
						db.getDate(6), db.getDate(7), db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11));
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
		return null;
	}
	
	public int getTotalRowCnt() {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select count(1) cnt from encoding_queue");
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
