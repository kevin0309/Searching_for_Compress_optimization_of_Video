package framework.servlet.fileRequest.upload.logic.PB;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import framework.util.windowsAppProcessing.WindowsAppProcessOptions;

/**
 * FFPROBE 사용하여 비디오 width, height 구하는 프로세스
 * @author 박유현
 * @since 2019.10.13
 */
public class GetVideoResolutionOptions implements WindowsAppProcessOptions {

	private ArrayList<String> tempArr;
	
	
	public GetVideoResolutionOptions(String ffprobePath, String videoFilePath) {
		tempArr = new ArrayList<>();
		tempArr.add(ffprobePath);
		tempArr.add("-v");
		tempArr.add("error");
		tempArr.add("-select_streams");
		tempArr.add("v:0");
		tempArr.add("-show_entries");
		tempArr.add("stream=width,height");
		tempArr.add("-of");
		tempArr.add("csv=p=0");
		tempArr.add(videoFilePath);
	}

	@Override
	public String[] generateCmdLine() {
		String[] result = new String[tempArr.size()];
		for (int i = 0; i < tempArr.size(); i++)
			result[i] = tempArr.get(i);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray generateJSONCmdLine() {
		JSONArray result = new JSONArray();
		for (String opt : tempArr)
			result.add(opt);
		return result;
	}

}
