package indexPage.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import framework.init.ServerConfig;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.DateUtil;
import framework.util.LogUtil;
import works.encoding.EncodingQueueDAO;
import works.encoding.EncodingQueueVO;

public class GetEncodingQueueListAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/getEncodingQueueList";
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
		EncodingQueueDAO dao = new EncodingQueueDAO();
		
		//비디오 리스트 조회
		ArrayList<EncodingQueueVO> eqList = dao.getEncodingQueueList(ServerConfig.getServerId(), offset, amount);
		JSONArray eqList2 = new JSONArray();
		for (EncodingQueueVO eq : eqList) {
			JSONObject eq2 = new JSONObject();
			eq2.put("seq", eq.getSeq());
			eq2.put("fileId", eq.getFileId());
			eq2.put("presetCode", eq.getPresetCode());
			eq2.put("status", eq.getStatus());
			eq2.put("endVolume", eq.geteVolume());
			eq2.put("startDate", DateUtil.toString(eq.getsDate()));
			eq2.put("endDate", DateUtil.toString(eq.geteDate()));
			eq2.put("assignedServerId", eq.getAssignedServerId());
			eq2.put("regdate", DateUtil.toString(eq.getRegdate()));
			eqList2.add(eq2);
		}
		res.put("encodingQueueList", eqList2);
		
		JSONObject entries = new JSONObject();
		entries.put("offset", offset);
		entries.put("amount", amount);
		entries.put("total", dao.getTotalRowCnt(ServerConfig.getServerId()));
		res.put("result_entries", entries);
		
		return res;
	}

}
