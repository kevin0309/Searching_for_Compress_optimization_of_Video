package encodingPreset.command;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import encodingPreset.dao.EncodingPresetDAO;
import framework.servlet.controller.handler.AjaxRequestHandler;

public class UpdateEncodingPresetOptionsAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/updatePresetOptions";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String code = req.getParameter("presetCode");
		String data = req.getParameter("data");
		JSONParser parser = new JSONParser();
		JSONArray dataArr = (JSONArray) parser.parse(data);
		
		EncodingPresetDAO dao = new EncodingPresetDAO();
		dao.deletePresetOptions(code);
		
		for (int i = 0; i < dataArr.size(); i++) {
			JSONObject temp = (JSONObject) dataArr.get(i);
			String optionName = (String) temp.get("optionName");
			String optionValue = (String) temp.get("optionValue");
			int orderby = Integer.parseInt((String) temp.get("orderby"));
			dao.insertPresetOption(code, optionName, optionValue, orderby, new Date());
		}
		System.out.println(dataArr);
		return null;
	}

}
