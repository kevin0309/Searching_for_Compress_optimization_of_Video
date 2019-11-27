package encodingQueue.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONAware;

import encodingQueue.dao.EncodingQueueDAO;
import framework.servlet.controller.handler.AjaxRequestHandler;
import sampleVideo.dao.SampleVideoDAO;

public class AddQueueAction implements AjaxRequestHandler {

	@Override
	public String getURL() {
		return "/process/addQueue";
	}

	@Override
	public JSONAware process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int fid = Integer.parseInt(req.getParameter("seq"));
		String pCode = req.getParameter("presetCode");
		
		try {
			new EncodingQueueDAO().insertQueue(fid, new SampleVideoDAO().selectSampleVideoById(fid).getStorageServerId(), pCode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
