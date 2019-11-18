package test;

import java.io.File;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import framework.jdbc.DBMng;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.FileUtil;
import works.ssim.SsimCalculator;

public class TestSsim implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/test/ssim";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		System.out.println("Hello World!");
		int fid = Integer.parseInt(req.getParameter("fid"));
		String compPresetCode = req.getParameter("cpc");
		String refThumbIndex = req.getParameter("rti");
		String compThumbIndex = req.getParameter("cti");
		String dir = db(fid);
		String newDir = dir.substring(0, dir.lastIndexOf("."));
		String refImagePath = newDir + "/thumb" + refThumbIndex + ".jpg";
		String dir2 = db2(fid, compPresetCode);
		String newDir2 = dir2.substring(0, dir2.lastIndexOf("."));
		String compImagePath = newDir2 + "/thumb" + compThumbIndex + ".jpg";
		
		File refImageFile = new File(refImagePath);
		File compImageFile = new File(compImagePath);
		
		SsimCalculator sc = new SsimCalculator(refImageFile);
		double res = sc.compareTo(compImageFile);
		System.out.println("ssim : " + res);
		return null;
	}
	
	private String db(int fid) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from sample_video where seq = ?");
			db.setInt(fid);
			db.execute();
			
			if (db.next()) {
				return db.getString("directory");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return null;
	}
	
	private String db2(int fid, String compPresetCode) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_queue where file_id = ? and preset_code = ? order by seq desc");
			db.setInt(fid);
			db.setString(compPresetCode);
			db.execute();
			
			if (db.next()) {
				return db.getString("new_directory");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return null;
	}

}
