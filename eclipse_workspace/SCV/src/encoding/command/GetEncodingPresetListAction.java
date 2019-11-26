package encoding.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import encoding.dao.EncodingPresetDAO;
import encoding.vo.EncodingPresetOptionVO;
import encoding.vo.EncodingPresetVO;
import framework.servlet.controller.handler.AjaxRequestHandler;
import framework.util.DateUtil;

public class GetEncodingPresetListAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/getPresetList";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		JSONObject res = new JSONObject();
		EncodingPresetDAO dao = new EncodingPresetDAO();
		
		ArrayList<EncodingPresetVO> presetList = dao.getPresetList();
		JSONArray presetList2 = new JSONArray();
		for (EncodingPresetVO preset : presetList) {
			JSONObject preset2 = new JSONObject();
			preset2.put("code", preset.getCode());
			preset2.put("name", preset.getName());
			preset2.put("regdate", DateUtil.toString(preset.getRegdate()));
			
			JSONArray presetOpts = new JSONArray();
			for (EncodingPresetOptionVO opt : preset.getOpts()) {
				JSONObject opt2 = new JSONObject();
				opt2.put("seq", opt.getSeq());
				opt2.put("optionName", opt.getOptionName());
				opt2.put("optionValue", opt.getOptionValue());
				opt2.put("orderby", opt.getOrderBy());
				opt2.put("regdate", DateUtil.toString(preset.getRegdate()));
				presetOpts.add(opt2);
			}
			preset2.put("opts", presetOpts);
			presetList2.add(preset2);
		}
		res.put("serverList", presetList2);
		
		JSONObject entries = new JSONObject();
		entries.put("total", dao.getTotalRowCnt());
		res.put("result_entries", entries);
		
		return res;
	}

}
