package encoding.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import encoding.dao.EncodingServerDAO;
import encoding.vo.EncodingServerVO;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.DateUtil;
import framework.util.LogUtil;

public class GetEncodingServerListAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/getServerList";
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
		EncodingServerDAO dao = new EncodingServerDAO();
		
		ArrayList<EncodingServerVO> serverList = dao.getServerList(offset, amount);
		JSONArray serverList2 = new JSONArray();
		for (EncodingServerVO server : serverList) {
			JSONObject server2 = new JSONObject();
			server2.put("seq", server.getSeq());
			server2.put("desc", server.getDesc());
			server2.put("addr", server.getAddress());
			server2.put("macAddr", server.getMacAddress());
			server2.put("status", server.getStatus());
			server2.put("regdate", DateUtil.toString(server.getRegdate()));
			serverList2.add(server2);
		}
		res.put("serverList", serverList2);
		
		JSONObject entries = new JSONObject();
		entries.put("offset", offset);
		entries.put("amount", amount);
		entries.put("total", dao.getTotalRowCnt());
		res.put("result_entries", entries);
		
		return res;
	}

}
