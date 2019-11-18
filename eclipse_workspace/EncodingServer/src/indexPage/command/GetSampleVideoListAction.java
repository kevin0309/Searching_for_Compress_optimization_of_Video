package indexPage.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import framework.init.ServerConfig;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.servlet.fileRequest.SampleVideoDAO;
import framework.servlet.fileRequest.SampleVideoVO;
import framework.util.DateUtil;
import framework.util.LogUtil;

public class GetSampleVideoListAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/getSampleVideoList";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int offset;
		int amount;
		try {
			offset = Integer.parseInt(req.getParameter("offset"));
			amount = Integer.parseInt(req.getParameter("amount"));	
		} catch (NumberFormatException e) {
			LogUtil.printErrLog(req.getRemoteAddr(), "Invalid parameter!");
			return null;
		}
		
		JSONObject res = new JSONObject();
		SampleVideoDAO dao = new SampleVideoDAO();
		
		//비디오 리스트 조회
		ArrayList<SampleVideoVO> vList = dao.selectSampleVideoList(ServerConfig.getServerId(), offset, amount);
		JSONArray vList2 = new JSONArray();
		for (SampleVideoVO sv : vList) {
			JSONObject sv2 = new JSONObject();
			sv2.put("seq", sv.getSeq());
			sv2.put("fileName", sv.getFileName());
			sv2.put("fileExt", sv.getFileExt());
			sv2.put("mimeType", sv.getMimeType());
			sv2.put("fileVolume", sv.getVolumeStr());
			sv2.put("vCodec", sv.getvCodec());
			sv2.put("aCodec", sv.getaCodec());
			sv2.put("width", sv.getWidth());
			sv2.put("height", sv.getHeight());
			sv2.put("storedServerId", sv.getStorageServerId());
			sv2.put("regdate", DateUtil.toString(sv.getRegdate()));
			vList2.add(sv2);
		}
		res.put("fileList", vList2);
		
		JSONObject entries = new JSONObject();
		entries.put("offset", offset);
		entries.put("amount", amount);
		entries.put("total", dao.getTotalRowCnt(ServerConfig.getServerId()));
		res.put("result_entries", entries);
		
		return res;
	}

}
