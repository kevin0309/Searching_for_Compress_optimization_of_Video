package works;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import framework.jdbc.DBMng;
import framework.util.LogUtil;

/**
 * DB encoding_queue 테이블 대을 DAO
 * @author 박유현
 * @since 2019.10.13
 */
public class EncodingQueueDAO {
	
	/**
	 * 대기중인 다음 인코딩 작업 조회
	 * @return
	 */
	public EncodingQueueVO getNextEncodingWork(int curServerId) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue eq where assigned_server_id = ? and status = 'waiting' and file_id = (select seq from sample_video where seq = eq.file_id and sample_video.storage_server_id = ?) limit 1");
			db.setInt(curServerId);
			db.setInt(curServerId);
			db.execute();
			if (db.next()) {
				return new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), 
						db.getString(4), db.getLong(5), db.getDate(6), db.getDate(7), 
						db.getInt(8), db.getString(9), db.getDate(10));
			}
			else return null;
		} catch (Exception e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * encoding_queue.seq로 조회
	 * @param seq
	 * @return
	 */
	public EncodingQueueVO getEncodingWorkBySeq(int seq) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue where seq = ?");
			db.setInt(seq);
			db.execute();
			if (db.next()) {
				return new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), 
						db.getString(4), db.getLong(5), db.getDate(6), db.getDate(7), 
						db.getInt(8), db.getString(9), db.getDate(10));
			}
			else return null;
		} catch (Exception e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * 작업 시작 시 상태 전환
	 * @param seq
	 * @param status
	 * @param startDate
	 * @param serverId
	 */
	public void updateCurWorkStartStatus(int seq, Date startDate, int serverId) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'running', s_date = ?, assigned_server_id = ? where seq = ?");
			db.setDate(startDate);
			db.setInt(serverId);
			db.setInt(seq);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * 작업 완료 후 상태 전환
	 * @param seq
	 * @param endVolume
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkEndStatus(int seq, long endVolume, Date endDate, String newDirectory) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'finished', e_volume = ?, e_date = ?, new_directory = ? where seq = ?");
			db.setLong(endVolume);
			db.setDate(endDate);
			db.setString(newDirectory);
			db.setInt(seq);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public ArrayList<EncodingQueueVO> getEncodingQueueList(int offset, int amount) {
		DBMng db = null;
		ArrayList<EncodingQueueVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue order by seq desc limit ?, ?");
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next()) {
				res.add(new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), 
						db.getString(4), db.getLong(5), db.getDate(6), db.getDate(7), 
						db.getInt(8), db.getString(9), db.getDate(10)));
			}
			return res;
		} catch (Exception e) {
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
			db.setQuery("select count(1) cnt from encoding_queue");
			db.execute();
			
			if (db.next())
				return db.getInt("cnt");
			else
				return -1;
		} catch (Exception e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * encoding_queue.preset_code로 프리셋 옵션 리스트 조회
	 * @param encodingPreset
	 * @return
	 */
	public ArrayList<EncodingQueueOptionVO> getEncodingOptions(String encodingPreset) {
		DBMng db = null;
		ArrayList<EncodingQueueOptionVO> res = new ArrayList<>();
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_preset_option where preset_code = ? order by orderby");
			db.setString(encodingPreset);
			db.execute();
			
			while (db.next()){
				EncodingQueueOptionVO temp = new EncodingQueueOptionVO(db.getString("option_name"), db.getString("option_value"), db.getInt("orderby"));
				res.add(temp);
			}
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
		
		return res;
	}
}
