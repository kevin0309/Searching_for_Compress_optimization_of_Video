package framework.servlet.fileRequest;

import java.sql.SQLException;
import java.util.ArrayList;

import framework.jdbc.DBMng;
import framework.util.LogUtil;

/**
 * DB sample_video 테이블에 대응되는 DAO
 * @author 박유현
 * @since 2019.10.13
 */
public class SampleVideoDAO {

	/**
	 * 새로운 샘플 비디오를 추가
	 * @param req
	 */
	public void insertNewSampleVideo(SampleVideoVO req) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into sample_video values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			db.setString(req.getFileName());
			db.setString(req.getFileExt());
			db.setString(req.getMimeType());
			db.setLong(req.getVolume());
			db.setString(req.getVolumeStr());
			db.setString(req.getvCodec());
			db.setString(req.getaCodec());
			db.setInt(req.getWidth());
			db.setInt(req.getHeight());
			db.setInt(req.getStorageServerId());
			db.setString(req.getDirectory());
			db.setDate(req.getRegdate());
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	/**
	 * sample_video.seq로 조회
	 * @param seq
	 * @return
	 */
	public SampleVideoVO selectSampleVideoById(int seq) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from sample_video where seq = ?");
			db.setInt(seq);
			db.execute();
			
			if (db.next()) {
				SampleVideoVO temp = new SampleVideoVO(db.getInt("seq"), db.getString("name"), db.getString("extension"), db.getString("MIME_type"), db.getLong("volume"), db.getString("volume_str"), db.getInt("storage_server_id"), db.getString("directory"), db.getDate("regdate"));
				temp.setvCodec(db.getString("v_codec"));
				temp.setaCodec(db.getString("a_codec"));
				temp.setWidth(db.getInt("width"));
				temp.setHeight(db.getInt("height"));
				return temp;
			}
			else return null;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public ArrayList<SampleVideoVO> selectSampleVideoList(int offset, int amount) {
		DBMng db = null;
		ArrayList<SampleVideoVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from sample_video order by seq desc limit ?, ?");
			db.setInt(offset);
			db.setInt(amount);
			db.execute();
			
			while (db.next()) {
				SampleVideoVO temp = new SampleVideoVO(db.getInt("seq"), db.getString("name"), db.getString("extension"), db.getString("MIME_type"), db.getLong("volume"), db.getString("volume_str"), db.getInt("storage_server_id"), db.getString("directory"), db.getDate("regdate"));
				temp.setvCodec(db.getString("v_codec"));
				temp.setaCodec(db.getString("a_codec"));
				temp.setWidth(db.getInt("width"));
				temp.setHeight(db.getInt("height"));
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
	
	public int getTotalRowCnt() {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select count(1) cnt from sample_video");
			db.execute();
			
			if (db.next()) {
				return db.getInt("cnt");
			}
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
