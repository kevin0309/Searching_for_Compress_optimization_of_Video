package test;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import framework.jdbc.DBMng;
import framework.servlet.controller.handler.AjaxRequestHandler;

public class TestAddEncodingQueue implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/test/addQueue";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int fid = Integer.parseInt(req.getParameter("fid"));
		int sid = Integer.parseInt(req.getParameter("sid"));
		String pCode = req.getParameter("pCode");
		
		if (pCode == null) {
			db(fid, sid, "crf_18_ultrafast");
			db(fid, sid, "crf_18_superfast");
			db(fid, sid, "crf_18_veryfast");
			db(fid, sid, "crf_18_faster");
			db(fid, sid, "crf_18_fast");
			db(fid, sid, "crf_18_medium");
			db(fid, sid, "crf_18_slow");
			db(fid, sid, "crf_21_ultrafast");
			db(fid, sid, "crf_21_superfast");
			db(fid, sid, "crf_21_veryfast");
			db(fid, sid, "crf_21_faster");
			db(fid, sid, "crf_21_fast");
			db(fid, sid, "crf_21_medium");
			db(fid, sid, "crf_21_slow");
			db(fid, sid, "crf_24_ultrafast");
			db(fid, sid, "crf_24_superfast");
			db(fid, sid, "crf_24_veryfast");
			db(fid, sid, "crf_24_faster");
			db(fid, sid, "crf_24_fast");
			db(fid, sid, "crf_24_medium");
			db(fid, sid, "crf_24_slow");
			db(fid, sid, "crf_27_ultrafast");
			db(fid, sid, "crf_27_superfast");
			db(fid, sid, "crf_27_veryfast");
			db(fid, sid, "crf_27_faster");
			db(fid, sid, "crf_27_fast");
			db(fid, sid, "crf_27_medium");
			db(fid, sid, "crf_27_slow");
		}
		else
			db(fid, sid, pCode);
		
		return null;
	}
	
	private void db(int fid, int assingnedServerId, String presetCode) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into encoding_queue values (null, ?, ?, ?, null, null, null, ?, null, ?)");
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
