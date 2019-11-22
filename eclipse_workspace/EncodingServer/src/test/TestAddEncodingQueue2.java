package test;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import framework.jdbc.DBMng;
import framework.servlet.controller.handler.AjaxRequestHandler;

public class TestAddEncodingQueue2 implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/test/addQueue2";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int fid = Integer.parseInt(req.getParameter("fid"));
		int sid = Integer.parseInt(req.getParameter("sid"));
		String pCode = req.getParameter("pCode");
		
		if (pCode == null) {
			db(fid, sid, "crf_18_ultrafast");
			db(fid, sid, "crf_18_slow");
			db(fid, sid, "crf_27_ultrafast");
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
