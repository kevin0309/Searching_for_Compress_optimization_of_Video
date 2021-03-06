package works.encoding;

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
						db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11));
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
						db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11));
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
	 * encoding_queue.file_id, preset_code로 조회
	 * @param seq
	 * @return
	 */
	public EncodingQueueVO getEncodingWorkBySeq(int fileId, String presetCode) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue where file_id = ? and preset_code = ? order by seq desc limit 1");
			db.setInt(fileId);
			db.setString(presetCode);
			db.execute();
			if (db.next()) {
				return new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), 
						db.getString(4), db.getLong(5), db.getDate(6), db.getDate(7), 
						db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11));
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
			db.setQuery("update encoding_queue set status = 'encoding', s_date = ?, assigned_server_id = ? where seq = ?");
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
			db.setQuery("update encoding_queue set status = 'encoded', e_volume = ?, e_date = ?, new_directory = ? where seq = ?");
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
	
	/**
	 * 작업 실패 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkErrorStatus(int seq, Date endDate, String newDirectory) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'error_encoding', e_date = ?, new_directory = ? where seq = ?");
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
	
	/**
	 * 썸네일 추출 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkThumbEndStatus(int seq, Date endDate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'thumbnail', e_date = ? where seq = ?");
			db.setDate(endDate);
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
	 * 썸네일 추출 실패 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkThumbErrorStatus(int seq, Date endDate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'error_thumbnail', e_date = ? where seq = ?");
			db.setDate(endDate);
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
	 * SSIM 연산 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkSSIMEndStatus(int seq, Date endDate, double ssim) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'finished', e_date = ?, ssim = ? where seq = ?");
			db.setDate(endDate);
			db.setDouble(ssim);
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
	 * SSIM 연산 실패 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkSSIMErrorStatus(int seq, Date endDate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'error_SSIM', e_date = ? where seq = ?");
			db.setDate(endDate);
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
	 * SSIM 연산 대기 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkSSIMWaitStatus(int seq, Date endDate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'waiting_SSIM', e_date = ? where seq = ?");
			db.setDate(endDate);
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
	 * 완료 후 상태 전환
	 * @param seq
	 * @param endDate
	 * @param newDirectory
	 */
	public void updateCurWorkEndStatus(int seq, Date endDate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("update encoding_queue set status = 'finished', e_date = ? where seq = ?");
			db.setDate(endDate);
			db.setInt(seq);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public ArrayList<EncodingQueueVO> getEncodingQueueList(int assignedServerId, int offset, int amount) {
		DBMng db = null;
		ArrayList<EncodingQueueVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue where assigned_server_id = ? order by seq desc limit ?, ?");
			db.setInt(assignedServerId);
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next()) {
				res.add(new EncodingQueueVO(db.getInt(1), db.getInt(2), db.getString(3), 
						db.getString(4), db.getLong(5), db.getDate(6), db.getDate(7), 
						db.getInt(8), db.getDouble(9), db.getString(10), db.getDate(11)));
			}
			return res;
		} catch (Exception e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public int getTotalRowCnt(int assignedServerId) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select count(1) cnt from encoding_queue where assigned_server_id = ?");
			db.setInt(assignedServerId);
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
	
	public void insertQueue(int fid, int assingnedServerId, String presetCode) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into encoding_queue values (null, ?, ?, ?, null, null, null, ?, null, null, ?)");
			db.setInt(fid);
			db.setString(presetCode);
			db.setString("waiting");
			db.setInt(assingnedServerId);
			db.setDate(new Date());
			db.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
}
