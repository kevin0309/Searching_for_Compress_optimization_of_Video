package test;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import framework.servlet.controller.handler.AjaxRequestHandler;
import works.ssim.SsimCalculator;

public class TestSsim implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/test/ssim";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		System.out.println("Hello World!");
		
		File refImageFile = new File("E:/ssim_test.jpg");
		File compImageFile = new File("E:/ssim_test2.jpg");
		
		SsimCalculator sc = new SsimCalculator(refImageFile);
		double res = sc.compareTo(compImageFile);
		System.out.println("res : " + res);
		return null;
	}
/*	
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
	}*/

}
