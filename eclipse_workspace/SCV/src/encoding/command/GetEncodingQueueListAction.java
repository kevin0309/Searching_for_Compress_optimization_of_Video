package encoding.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import encoding.dao.EncodingQueueDAO;
import encoding.dao.SampleVideoDAO;
import encoding.vo.EncodingQueueVO;
import encoding.vo.SampleVideoVO;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.DateUtil;

public class GetEncodingQueueListAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/getEncodingQueue";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		EncodingQueueDAO dao = new EncodingQueueDAO();
		ArrayList<EncodingQueueVO> eqList;
		JSONObject res = new JSONObject();
		JSONObject entries = new JSONObject();
		try {
			int offset = Integer.parseInt(req.getParameter("offset"));
			int amount = Integer.parseInt(req.getParameter("amount"));
			entries.put("offset", offset);
			entries.put("amount", amount);
			eqList = dao.getEncodingQueueList(offset, amount);
		} catch (NumberFormatException e) {
			String indexesParam = req.getParameter("data");
			String[] indexes = indexesParam.split(",");
			eqList = new ArrayList<>();
			for (int i = 0; i < indexes.length; i++) {
				EncodingQueueVO tempQueue = dao.getEncodingQueue(Integer.parseInt(indexes[i]));
				if (tempQueue != null)
					eqList.add(tempQueue);
			}
		}
		
		//비디오 리스트 조회
		JSONArray eqList2 = new JSONArray();
		for (EncodingQueueVO eq : eqList) {
			SampleVideoDAO dao2 = new SampleVideoDAO();
			SampleVideoVO video = dao2.selectSampleVideoById(eq.getFileId());
			JSONObject eq2 = new JSONObject();
			eq2.put("seq", eq.getSeq());
			eq2.put("fileId", eq.getFileId());
			eq2.put("presetCode", eq.getPresetCode());
			eq2.put("status", eq.getStatus());
			eq2.put("endVolume", eq.geteVolume());
			if (eq.geteVolume() != 0)
				eq2.put("volumeDifference", video.getVolume() - eq.geteVolume());
			else
				eq2.put("volumeDifference", null);
			eq2.put("startDate", DateUtil.toString(eq.getsDate()));
			eq2.put("endDate", DateUtil.toString(eq.geteDate()));
			if (eq.getsDate() != null && eq.geteDate() != null)
				eq2.put("elapsedTime", eq.geteDate().getTime() - eq.getsDate().getTime());
			else
				eq2.put("elapsedTime", null);
			eq2.put("assignedServerId", eq.getAssignedServerId());
			eq2.put("ssim", eq.getSsim());
			eq2.put("regdate", DateUtil.toString(eq.getRegdate()));
			eqList2.add(eq2);
		}
		res.put("encodingQueueList", eqList2);
		
		entries.put("total", dao.getTotalRowCnt());
		res.put("result_entries", entries);
		
		return res;
	}

}
