package framework.servlet.fileRequest.upload.logic.PB;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import framework.util.windowsAppProcessing.WindowsAppProcessOptions;

/**
 * FFPROBE 사용하여 비디오 width, height 구하는 프로세스
 * @author 박유현
 * @since 2019.10.13
 */
public class GetVideoMetadataOptions implements WindowsAppProcessOptions {

	private ArrayList<String> tempArr;
	
	
	public GetVideoMetadataOptions(String ffprobePath, String videoFilePath) {
		tempArr = new ArrayList<>();
		tempArr.add(ffprobePath);
		tempArr.add("-v"); 					//log level
		tempArr.add("error");				//Show all errors, including ones which can be recovered from.
		//tempArr.add("-select_streams");	//스트림 지정		
		//tempArr.add("v:0");				//비디오스트림 첫번째
		tempArr.add("-show_entries");		//엔트리 출력 (엔트리 종류 : format :파일 형식에 대한 정보, stream :파일을 이루는 각 스트림에 대한 정보, packet :파일을 구성하는 모든 패킷에 대한 정보)
		tempArr.add("stream=codec_name,codec_type,width,height");
		tempArr.add("-of");					//출력 형태 (default, csv, flat, ini, json, xml)
		//tempArr.add("csv=p=0");
		tempArr.add("json=c=1");
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
