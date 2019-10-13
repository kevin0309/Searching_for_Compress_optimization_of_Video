package framework.servlet.fileRequest;

import java.sql.SQLException;

import framework.jdbc.DBMng;
import framework.servlet.fileRequest.upload.logic.UploadSampleVideoRequestVO;

public class FileDAO {

	public void insertNewSampleVideo(UploadSampleVideoRequestVO req) {
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
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
}
