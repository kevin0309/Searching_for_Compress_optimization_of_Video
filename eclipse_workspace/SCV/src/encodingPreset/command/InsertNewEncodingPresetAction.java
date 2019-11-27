package encodingPreset.command;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import encodingPreset.dao.EncodingPresetDAO;
import framework.servlet.controller.handler.AjaxRequestHandler;

public class InsertNewEncodingPresetAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/insertPreset";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String code = req.getParameter("presetCode");
		String name = req.getParameter("desc");
		
		if (code == null || code.isEmpty())
			throw new RuntimeException("invalid preset code");
		
		EncodingPresetDAO dao = new EncodingPresetDAO();
		dao.insertNewPreset(code, name, new Date());
		return null;
	}

}
